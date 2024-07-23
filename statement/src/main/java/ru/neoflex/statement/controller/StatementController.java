package ru.neoflex.statement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.calculator.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
public class StatementController implements StatementApi {

    @Override
    @PostMapping
    public ResponseEntity<List<LoanOfferDto>> prescoreAndGenerateLoanOffers(
            @RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {
        return null;
    }

    @Override
    @PostMapping("/offer")
    public ResponseEntity<Void> chooseLoanOffer(@RequestBody LoanOfferDto loanOfferDto) {
        return null;
    }
}
