package ru.neoflex.statement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.neoflex.calculator.dto.exception.CreditDeniedException;
import ru.neoflex.calculator.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;
import ru.neoflex.calculator.dto.exception.PrescoringFailedException;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DealApiService {

    @Value("${statement.deal-statement-url}")
    private String dealStatementUrl;
    @Value("${statement.deal-offer-select-url}")
    private String dealOfferSelectUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<LoanOfferDto> registerStatementAndGenerateOffers(LoanStatementRequestDto loanStatementRequestDto) {
        HttpEntity<LoanStatementRequestDto> requestEntity
                = new HttpEntity<>(loanStatementRequestDto, getDefaultHeaders());

        ResponseEntity<LoanOfferDto[]> responseEntity = restTemplate
                .postForEntity(dealStatementUrl, requestEntity, LoanOfferDto[].class);

        if (responseEntity.getStatusCodeValue() == 400) {
            throw new PrescoringFailedException("");
        }

        return Arrays.asList(responseEntity.getBody());
    }

    public void selectLoanOffer(LoanOfferDto loanOfferDto) {
        HttpEntity<LoanOfferDto> requestEntity
                = new HttpEntity<>(loanOfferDto, getDefaultHeaders());

        ResponseEntity<Void> responseEntity = restTemplate
                .postForEntity(dealOfferSelectUrl, requestEntity, Void.class);

        if (responseEntity.getStatusCode().value() == 400) {
            throw new CreditDeniedException("");
        }
    }

    private HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
