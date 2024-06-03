package steammachinist.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import steammachinist.dto.CreditDto;
import steammachinist.dto.LoanOfferDto;
import steammachinist.dto.LoanStatementRequestDto;
import steammachinist.dto.ScoringDataDto;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/calculator")
public class CalculatorController implements CalculatorApi {

    @Override
    @PostMapping("/offers")
    public Collection<LoanOfferDto> generatePossibleLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        return List.of();
    }

    @Override
    @PostMapping("/calc")
    public CreditDto calculateFullCreditData(ScoringDataDto scoringDataDto) {
        return null;
    }
}
