package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.dto.exception.CreditDeniedException;
import ru.neoflex.calculator.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;
import ru.neoflex.calculator.dto.scoring.response.CreditDto;
import ru.neoflex.deal.configuration.KafkaTopic;
import ru.neoflex.deal.dto.email.EmailMessage;
import ru.neoflex.deal.dto.finishregistration.request.FinishRegistrationRequestDto;
import ru.neoflex.deal.entity.*;
import ru.neoflex.deal.mapper.CreditMapper;
import ru.neoflex.deal.mapper.FinishRegistrationRequestMapper;
import ru.neoflex.deal.mapper.LoanStatementRequestMapper;
import ru.neoflex.deal.mapper.ScoringDataMapper;
import ru.neoflex.deal.service.persistance.ClientService;
import ru.neoflex.deal.service.persistance.CreditService;
import ru.neoflex.deal.service.persistance.StatementService;

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

        kafkaProducerService.send(KafkaTopic.FINISH_REGISTRATION, new EmailMessage(statement));
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

        } catch (CreditDeniedException creditDeniedException) {
            setNewStatusAndAppendHistory(statement, ApplicationStatus.CC_DENIED);
            kafkaProducerService.send(KafkaTopic.STATEMENT_DENIED, new EmailMessage(statement));
            throw creditDeniedException;

        } finally {
            statementService.updateStatement(statement);
        }
    }
