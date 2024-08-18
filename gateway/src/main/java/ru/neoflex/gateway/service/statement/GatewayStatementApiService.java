package ru.neoflex.gateway.service.statement;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;
import ru.neoflex.gateway.service.RequestService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GatewayStatementApiService {

    private final RequestService requestService;

    @Value("${gateway.statement-url}")
    private String statementUrl;

    public List<LoanOfferDto> prescoreAndGenerateLoanOffers(
            LoanStatementRequestDto loanStatementRequestDto) {
        return Arrays.asList(requestService.forwardRequest(
                statementUrl,
                StatementOperation.PRESCORE_AND_GENERATE_OFFERS.getPath(),
                HttpMethod.POST,
                Optional.of(loanStatementRequestDto),
                LoanOfferDto[].class)
        );
    }

    public void chooseLoanOffer(LoanOfferDto loanOfferDto) {
        requestService.forwardRequest(
                statementUrl,
                StatementOperation.CHOOSE_OFFER.getPath(),
                HttpMethod.POST,
                Optional.of(loanOfferDto),
                Void.class
        );
    }
}
