package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.neoflex.calculator.dto.exception.CreditDeniedException;
import ru.neoflex.calculator.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;
import ru.neoflex.calculator.dto.scoring.request.ScoringDataDto;
import ru.neoflex.calculator.dto.scoring.response.CreditDto;
import ru.neoflex.calculator.dto.exception.PrescoringFailedException;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculatorApiService {

    @Value("${deal.calculator-offers-url}")
    private String calculatorOffersUrl;
    @Value("${deal.calculator-calc-url}")
    private String calculatorCalcUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<LoanOfferDto> getPossibleOffers(LoanStatementRequestDto loanStatementRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoanStatementRequestDto> request = new HttpEntity<>(loanStatementRequestDto, headers);

        ResponseEntity<LoanOfferDto[]> response = restTemplate.exchange(
                calculatorOffersUrl,
                HttpMethod.POST,
                request,
                LoanOfferDto[].class
        );

        if (response.getStatusCode().value() == 400) {
            throw new PrescoringFailedException("");
        }

        return Arrays.asList(response.getBody());
    }

    public CreditDto getFullCreditData(ScoringDataDto scoringDataDto) throws CreditDeniedException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ScoringDataDto> request = new HttpEntity<>(scoringDataDto, headers);

        ResponseEntity<CreditDto> response = restTemplate.exchange(
                calculatorCalcUrl,
                HttpMethod.POST,
                request,
                CreditDto.class
        );

        if (response.getStatusCode().value() == 400) {
            throw new CreditDeniedException("");
        }

        return response.getBody();
    }
}
