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
        Client client = loanStatementRequestMapper.mapClient(loanStatementRequestDto);
        client.setPassport(loanStatementRequestMapper.mapPassport(loanStatementRequestDto));
        client = clientService.addClient(client);

        Statement statement = loanStatementRequestMapper.mapStatement(loanStatementRequestDto);
        statement.setClient(client);
        statement.setStatus(ApplicationStatus.PREAPPROVAL);
        statement.setStatusHistory(new ArrayList<>());
        statement = statementService.addStatement(statement);

        List<LoanOfferDto> offers = calculatorApiService.getPossibleOffers(loanStatementRequestDto);
        Statement finalStatement = statement;
        return offers.stream()
                .map(loanOfferDto -> loanOfferDto.withStatementId(finalStatement.getStatementId()))
                .toList();
    }

    public void chooseLoanOffer(LoanOfferDto loanOfferDto) {

    }

    public void finishRegistrationAndCalculateCredit(String statementId,
                                                     FinishRegistrationRequestDto finishRegistrationRequestDto) {

    }
}
