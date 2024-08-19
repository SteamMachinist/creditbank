package ru.neoflex.statement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.common.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.common.dto.offer.response.LoanOfferDto;
import ru.neoflex.statement.service.StatementService;

import java.util.List;

@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
public class StatementController implements StatementApi {

    private final StatementService statementService;

    @Override
    @PostMapping
    public ResponseEntity<List<LoanOfferDto>> prescoreAndGenerateLoanOffers(
            @RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        return ResponseEntity.ok(statementService.prescoreAndGenerateLoanOffers(loanStatementRequestDto));
    }

    @Override
    @PostMapping("/offer")
    public ResponseEntity<Void> chooseLoanOffer(@RequestBody LoanOfferDto loanOfferDto) {
        statementService.chooseLoanOffer(loanOfferDto);
        return ResponseEntity.ok().build();
    }
}
