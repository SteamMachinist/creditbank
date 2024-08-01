package ru.neoflex.statement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatementService {

    private final DealApiService dealApiService;

    public List<LoanOfferDto> prescoreAndGenerateLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        return dealApiService.registerStatementAndGenerateOffers(loanStatementRequestDto);
    }

    public void chooseLoanOffer(LoanOfferDto loanOfferDto) {
        dealApiService.selectLoanOffer(loanOfferDto);
    }
}
