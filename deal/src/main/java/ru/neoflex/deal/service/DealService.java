package ru.neoflex.deal.service;

import org.springframework.stereotype.Service;
import ru.neoflex.calculator.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;
import ru.neoflex.deal.dto.finishregistration.request.FinishRegistrationRequestDto;

import java.util.List;

@Service
public class DealService {

    public List<LoanOfferDto> initialRegisterAndGenerateLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        return null;
    }

    public void chooseLoanOffer(LoanOfferDto loanOfferDto) {

    }

    public void finishRegistrationAndCalculateCredit(String statementId,
                                                     FinishRegistrationRequestDto finishRegistrationRequestDto) {

    }
}
