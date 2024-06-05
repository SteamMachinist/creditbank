package steammachinist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import steammachinist.dto.scoring.response.CreditDto;
import steammachinist.dto.offer.response.LoanOfferDto;
import steammachinist.dto.offer.request.LoanStatementRequestDto;
import steammachinist.dto.scoring.request.ScoringDataDto;
import steammachinist.service.CalculatorService;

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
