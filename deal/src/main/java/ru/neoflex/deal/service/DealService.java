package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;
import ru.neoflex.calculator.dto.scoring.request.ScoringDataDto;
import ru.neoflex.calculator.dto.scoring.response.CreditDto;
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
import java.util.ArrayList;
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
        addCurrentStatusToHistory(statement);
        statement.setStatus(ApplicationStatus.APPROVED);
        statementService.updateStatement(statement);
    }

    private void addCurrentStatusToHistory(Statement statement) {
        statement.getStatusHistory().add(
                new StatusHistoryElement(
                        statement.getStatus(),
                        new Timestamp(System.currentTimeMillis()),
                        ChangeType.AUTOMATIC));
    }

    public void finishRegistrationAndCalculateCredit(String statementId,
                                                     FinishRegistrationRequestDto finishRegistrationRequestDto) {
        Statement statement = statementService.getStatementById(UUID.fromString(statementId));
        finishRegistrationRequestMapper.updateClient(statement.getClient(), finishRegistrationRequestDto);
        statement = statementService.updateStatement(statement);

        CreditDto creditDto = calculatorApiService.getFullCreditData(scoringDataMapper.map(statement));
        Credit credit = creditMapper.map(creditDto);
        credit.setCreditStatus(CreditStatus.CALCULATED);
        credit = creditService.addCredit(credit);

        statement.setCredit(credit);
        addCurrentStatusToHistory(statement);
        statement.setStatus(ApplicationStatus.CC_APPROVED);
        statementService.updateStatement(statement);
    }
}
