package ru.neoflex.gateway.controller.statement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.calculator.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;
import ru.neoflex.gateway.service.statement.GatewayStatementApiService;

import java.util.List;

@RestController
@RequestMapping("/gateway/statement")
@RequiredArgsConstructor
public class GatewayStatementController implements GatewayStatementApi {

    private final GatewayStatementApiService statementApiService;

    @Override
    @PostMapping
    public ResponseEntity<List<LoanOfferDto>> prescoreAndGenerateLoanOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        return ResponseEntity.ok(statementApiService.prescoreAndGenerateLoanOffers(loanStatementRequestDto));
    }

    @Override
    @PostMapping("/offer")
    public ResponseEntity<Void> chooseLoanOffer(@RequestBody LoanOfferDto loanOfferDto) {
        statementApiService.chooseLoanOffer(loanOfferDto);
        return ResponseEntity.ok().build();
    }
}
