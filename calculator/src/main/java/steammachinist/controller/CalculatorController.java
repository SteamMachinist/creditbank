package steammachinist.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import steammachinist.dto.respoonse.scoring.CreditDto;
import steammachinist.dto.respoonse.offer.LoanOfferDto;
import steammachinist.dto.request.offer.LoanStatementRequestDto;
import steammachinist.dto.request.scoring.ScoringDataDto;

import java.util.List;

@RestController
@RequestMapping("/calculator")
public class CalculatorController implements CalculatorApi {

    @Override
    @PostMapping("/offers")
    public List<LoanOfferDto> generatePossibleLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        return List.of();
    }

    @Override
    @PostMapping("/calc")
    public CreditDto calculateFullCreditData(ScoringDataDto scoringDataDto) {
        return null;
    }
}
