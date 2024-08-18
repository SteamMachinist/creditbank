package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.common.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.common.dto.offer.response.LoanOfferDto;
import ru.neoflex.common.dto.scoring.response.CreditDto;
import ru.neoflex.common.dto.email.KafkaTopic;
import ru.neoflex.common.dto.request.FinishRegistrationRequestDto;
import ru.neoflex.common.dto.statement.ApplicationStatus;
import ru.neoflex.common.dto.statement.ChangeType;
import ru.neoflex.common.dto.statement.credit.CreditStatus;
import ru.neoflex.common.dto.statement.StatusHistoryElement;
import ru.neoflex.common.exception.CreditDeniedException;
import ru.neoflex.deal.entity.*;
import ru.neoflex.common.exception.SesException;
import ru.neoflex.deal.mapper.CreditMapper;
import ru.neoflex.deal.mapper.FinishRegistrationRequestMapper;
import ru.neoflex.deal.mapper.LoanStatementRequestMapper;
import ru.neoflex.deal.mapper.ScoringDataMapper;
import ru.neoflex.deal.service.persistance.ClientService;
import ru.neoflex.deal.service.persistance.CreditService;
import ru.neoflex.deal.service.persistance.StatementService;
import ru.neoflex.common.dto.email.EmailMessage;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealService {

    private final ClientService clientService;
    private final StatementService statementService;
    private final CalculatorApiService calculatorApiService;
    private final LoanStatementRequestMapper loanStatementRequestMapper;
    private final FinishRegistrationRequestMapper finishRegistrationRequestMapper;
    private final ScoringDataMapper scoringDataMapper;
    private final CreditMapper creditMapper;
    private final CreditService creditService;
    private final KafkaProducerService kafkaProducerService;

    public List<LoanOfferDto> initialRegisterAndGenerateLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        Client client = clientService.addClient(
                loanStatementRequestMapper.mapClient(loanStatementRequestDto));

        Statement statement = statementService.addStatement(
                new Statement(client));

        List<LoanOfferDto> offers = calculatorApiService.getPossibleOffers(loanStatementRequestDto);

        return offers.stream()
                .map(loanOfferDto -> loanOfferDto.withStatementId(statement.getStatementId()))
                .toList();
    }

    public void chooseLoanOffer(LoanOfferDto loanOfferDto) {
        Statement statement = statementService.getStatementById(loanOfferDto.statementId());
        statement.setAppliedOffer(loanOfferDto);
        setNewStatusAndAppendHistory(statement, ApplicationStatus.APPROVED);
        statementService.updateStatement(statement);

        kafkaProducerService.send(fromStatement(statement, KafkaTopic.FINISH_REGISTRATION));
    }

    private void setNewStatusAndAppendHistory(Statement statement, ApplicationStatus status) {
        statement.getStatusHistory().add(
                new StatusHistoryElement(
                        statement.getStatus(),
                        new Timestamp(System.currentTimeMillis()),
                        ChangeType.AUTOMATIC));
        statement.setStatus(status);
    }

    public void finishRegistrationAndCalculateCredit(String statementId,
                                                     FinishRegistrationRequestDto finishRegistrationRequestDto) {
        Statement statement = statementService.getStatementById(UUID.fromString(statementId));
        finishRegistrationRequestMapper.updateClient(statement.getClient(), finishRegistrationRequestDto);
        statement = statementService.updateStatement(statement);

        try {
            CreditDto creditDto = calculatorApiService.getFullCreditData(scoringDataMapper.map(statement));
            Credit credit = creditMapper.map(creditDto);
            credit.setCreditStatus(CreditStatus.CALCULATED);
            credit = creditService.addCredit(credit);
            statement.setCredit(credit);
            setNewStatusAndAppendHistory(statement, ApplicationStatus.CC_APPROVED);
            kafkaProducerService.send(fromStatement(statement, KafkaTopic.CREATE_DOCUMENTS));

        } catch (CreditDeniedException creditDeniedException) {
            setNewStatusAndAppendHistory(statement, ApplicationStatus.CC_DENIED);
            kafkaProducerService.send(fromStatement(statement, KafkaTopic.STATEMENT_DENIED));
            throw creditDeniedException;

        } finally {
            statementService.updateStatement(statement);
        }
    }

    public void prepareAndSendDocuments(String statementId) {
        Statement statement = statementService.getStatementById(UUID.fromString(statementId));

        setNewStatusAndAppendHistory(statement, ApplicationStatus.PREPARE_DOCUMENTS);
        statement = statementService.updateStatement(statement);

        // documents creation

        setNewStatusAndAppendHistory(statement, ApplicationStatus.DOCUMENT_CREATED);
        statement = statementService.updateStatement(statement);
        kafkaProducerService.send(fromStatement(statement, KafkaTopic.SEND_DOCUMENTS, "*документы*"));
    }

    public void prepareAndSendSign(String statementId) {
        Statement statement = statementService.getStatementById(UUID.fromString(statementId));
        statement.setSesCode(UUID.randomUUID().toString());
        statement = statementService.updateStatement(statement);
        kafkaProducerService.send(fromStatement(statement, KafkaTopic.SEND_SES, statement.getSesCode()));
    }

    public void codeSignDocuments(String statementId, String sesCode) {
        Statement statement = statementService.getStatementById(UUID.fromString(statementId));

        if (!statement.getSesCode().equals(sesCode)) {
            throw new SesException("SES code mismatch");
        }

        statement.setSignDate(new Timestamp(System.currentTimeMillis()));
        statement.setStatus(ApplicationStatus.DOCUMENT_SIGNED);
        statement = statementService.updateStatement(statement);

        statement.setStatus(ApplicationStatus.CREDIT_ISSUED);
        statement = statementService.updateStatement(statement);
        kafkaProducerService.send(fromStatement(statement, KafkaTopic.CREDIT_ISSUED));
    }

    private EmailMessage fromStatement(Statement statement, KafkaTopic theme) {
        return new EmailMessage(
                statement.getClient().getEmail(),
                theme,
                statement.getStatementId());
    }

    private EmailMessage fromStatement(Statement statement, KafkaTopic theme, String aditionalInfo) {
        return new EmailMessage(
                statement.getClient().getEmail(),
                theme,
                statement.getStatementId(),
                aditionalInfo);
    }
}
