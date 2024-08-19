package ru.neoflex.gateway.controller.deal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.common.dto.request.FinishRegistrationRequestDto;
import ru.neoflex.common.dto.statement.StatementDto;
import ru.neoflex.gateway.service.deal.GatewayDealApiService;

import java.util.List;

@RestController
@RequestMapping("/gateway/deal")
@RequiredArgsConstructor
public class GatewayDealController implements GatewayDealApi {

    private final GatewayDealApiService dealApiService;

    @Override
    @PostMapping("/calculate/{statementId}")
    public ResponseEntity<Void> finishRegistrationAndCalculateCredit(
            @PathVariable String statementId,
            @RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto) {
        dealApiService.finishRegistrationAndCalculateCredit(statementId, finishRegistrationRequestDto);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/document/{statementId}/send")
    public ResponseEntity<Void> requestSendDocuments(@PathVariable String statementId) {
        dealApiService.requestSendDocuments(statementId);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/document/{statementId}/sign")
    public ResponseEntity<Void> requestSignDocuments(@PathVariable String statementId) {
        dealApiService.requestSignDocuments(statementId);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/document/{statementId}/code")
    public ResponseEntity<Void> codeSignDocuments(
            @PathVariable String statementId, @RequestBody String sesCode) {
        dealApiService.codeSignDocuments(statementId, sesCode);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/admin/statement/{statementId}")
    public ResponseEntity<StatementDto> getStatementById(@PathVariable String statementId) {
        return ResponseEntity.ok(dealApiService.getStatementById(statementId));
    }

    @Override
    @GetMapping("/admin/statement")
    public ResponseEntity<List<StatementDto>> getAllStatements() {
        return ResponseEntity.ok(dealApiService.getAllStatements());
    }
}
