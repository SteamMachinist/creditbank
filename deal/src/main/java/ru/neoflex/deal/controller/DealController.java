package ru.neoflex.deal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.calculator.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;
import ru.neoflex.deal.dto.finishregistration.request.FinishRegistrationRequestDto;

import java.util.List;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController implements DealApi {

    @Override
    @PostMapping("/statement")
    public ResponseEntity<List<LoanOfferDto>> generatePossibleLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        return null;
    }

    @Override
    @PostMapping("/offer/select")
    public ResponseEntity<Void> chooseLoanOffer(LoanOfferDto loanOfferDto) {
        return null;
    }

    @Override
    @PostMapping("/calculate/{statementId}")
    public ResponseEntity<Void> finishRegistrationAndCalculateCredit(@RequestParam String statementId,
                                                                     FinishRegistrationRequestDto finishRegistrationRequestDto) {
        return null;
    }
}
