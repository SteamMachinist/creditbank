package ru.neoflex.calculator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.common.dto.scoring.response.CreditDto;
import ru.neoflex.common.dto.offer.response.LoanOfferDto;
import ru.neoflex.common.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.common.dto.scoring.request.ScoringDataDto;
import ru.neoflex.calculator.service.CalculatorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/calculator")
@RequiredArgsConstructor
public class CalculatorController implements CalculatorApi {

    private final CalculatorService calculatorService;

    @Override
    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> generatePossibleLoanOffers(
            @Valid @RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        return ResponseEntity.ok(calculatorService.prescoreAndGenerateOffers(loanStatementRequestDto));
    }

    @Override
    @PostMapping("/calc")
    public ResponseEntity<CreditDto> calculateFullCreditData(@Valid @RequestBody ScoringDataDto scoringDataDto) {
        return ResponseEntity.ok(calculatorService.scoreAndCalculateCredit(scoringDataDto));
    }
}
