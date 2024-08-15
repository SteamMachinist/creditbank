package ru.neoflex.deal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.calculator.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;
import ru.neoflex.deal.dto.finishregistration.request.FinishRegistrationRequestDto;
import ru.neoflex.deal.service.DealService;

import java.util.List;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController implements DealApi {

    private final DealService dealService;

    @Override
    @PostMapping("/statement")
    public ResponseEntity<List<LoanOfferDto>> initialRegisterAndGenerateLoanOffers(
            @RequestBody LoanStatementRequestDto loanStatementRequestDto) {

        return ResponseEntity.ok(dealService.initialRegisterAndGenerateLoanOffers(loanStatementRequestDto));
    }

    @Override
    @PostMapping("/offer/select")
    public ResponseEntity<Void> chooseLoanOffer(@RequestBody LoanOfferDto loanOfferDto) {
        dealService.chooseLoanOffer(loanOfferDto);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/calculate/{statementId}")
    public ResponseEntity<Void> finishRegistrationAndCalculateCredit(
            @PathVariable String statementId,
            @RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto) {

        dealService.finishRegistrationAndCalculateCredit(statementId, finishRegistrationRequestDto);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/document/{statementId}/send")
    public ResponseEntity<Void> requestSendDocuments(@PathVariable String statementId) {
        dealService.prepareAndSendDocuments(statementId);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/document/{statementId}/sign")
    public ResponseEntity<Void> requestSignDocuments(@PathVariable String statementId) {
        dealService.prepareAndSendSign(statementId);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/document/{statementId}/code")
    public ResponseEntity<Void> codeSignDocuments(@PathVariable String statementId) {
        dealService.codeSignDocuments(statementId);
        return ResponseEntity.ok().build();
    }
}
