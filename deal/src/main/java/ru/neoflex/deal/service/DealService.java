package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;
import ru.neoflex.deal.dto.finishregistration.request.FinishRegistrationRequestDto;
import ru.neoflex.deal.entity.*;
import ru.neoflex.deal.mapper.LoanStatementRequestMapper;
import ru.neoflex.deal.service.persistance.ClientService;
import ru.neoflex.deal.service.persistance.StatementService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DealService {

    private final ClientService clientService;
    private final StatementService statementService;
    private final CalculatorApiService calculatorApiService;
    private final LoanStatementRequestMapper loanStatementRequestMapper;

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

    }
}
