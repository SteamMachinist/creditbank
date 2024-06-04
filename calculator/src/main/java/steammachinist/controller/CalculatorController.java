package steammachinist.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import steammachinist.dto.scoring.response.CreditDto;
import steammachinist.dto.offer.response.LoanOfferDto;
import steammachinist.dto.offer.request.LoanStatementRequestDto;
import steammachinist.dto.scoring.request.ScoringDataDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/calculator")
public class CalculatorController implements CalculatorApi {

    @Override
    @PostMapping("/offers")
    public List<LoanOfferDto> generatePossibleLoanOffers(@Valid @RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        return List.of();
    }

    @Override
    @PostMapping("/calc")
    public CreditDto calculateFullCreditData(@Valid @RequestBody ScoringDataDto scoringDataDto) {
        return null;
    }
}
