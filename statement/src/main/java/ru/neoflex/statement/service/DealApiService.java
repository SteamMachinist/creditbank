package ru.neoflex.statement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.neoflex.common.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.common.dto.offer.response.LoanOfferDto;
import ru.neoflex.common.exception.PrescoringFailedException;
import ru.neoflex.statement.exception.DealException;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DealApiService {

    @Value("${statement.deal-statement-url}")
    private String dealStatementUrl;
    @Value("${statement.deal-offer-select-url}")
    private String dealOfferSelectUrl;

    private final int VALIDATION_ERROR_CODE = 400;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<LoanOfferDto> registerStatementAndGenerateOffers(LoanStatementRequestDto loanStatementRequestDto) {
        HttpEntity<LoanStatementRequestDto> requestEntity
                = new HttpEntity<>(loanStatementRequestDto, getDefaultHeaders());

        ResponseEntity<LoanOfferDto[]> responseEntity = restTemplate
                .postForEntity(dealStatementUrl, requestEntity, LoanOfferDto[].class);

        if (responseEntity.getStatusCodeValue() == VALIDATION_ERROR_CODE) {
            throw new PrescoringFailedException("Validation failed");
        }

        return Arrays.asList(responseEntity.getBody());
    }

    public void selectLoanOffer(LoanOfferDto loanOfferDto) {
        HttpEntity<LoanOfferDto> requestEntity
                = new HttpEntity<>(loanOfferDto, getDefaultHeaders());

        ResponseEntity<Void> responseEntity = restTemplate
                .postForEntity(dealOfferSelectUrl, requestEntity, Void.class);

        if (responseEntity.getStatusCode().isError()) {
            throw new DealException("Loan offer select failed");
        }
    }

    private HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
