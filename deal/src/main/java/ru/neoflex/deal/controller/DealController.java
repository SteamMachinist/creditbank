package ru.neoflex.deal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.calculator.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;
import ru.neoflex.deal.dto.finishregistration.request.FinishRegistrationRequestDto;
import ru.neoflex.deal.entity.Statement;
import ru.neoflex.deal.service.DealService;
import ru.neoflex.deal.service.persistance.StatementService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController implements DealApi {

    private final DealService dealService;
    private final StatementService statementService;

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
    public ResponseEntity<Void> codeSignDocuments(@PathVariable String statementId, @RequestBody String sesCode) {
        dealService.codeSignDocuments(statementId, sesCode);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/admin/statement/{statementId}")
    public ResponseEntity<Statement> getStatementById(@PathVariable String statementId) {
        return ResponseEntity.ok(statementService.getStatementById(UUID.fromString(statementId)));
    }

    @Override
    @GetMapping("/admin/statement")
    public ResponseEntity<List<Statement>> getAllStatements() {
        return ResponseEntity.ok(statementService.getAllStatements());
    }
}
