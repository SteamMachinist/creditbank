package ru.neoflex.gateway.service.deal;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.dto.finishregistration.request.FinishRegistrationRequestDto;
import ru.neoflex.deal.entity.Statement;
import ru.neoflex.gateway.service.RequestService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GatewayDealApiService {

    private final RequestService requestService;

    @Value("${gateway.deal-url}")
    private String dealUrl;

    public void finishRegistrationAndCalculateCredit(
            String statementId, FinishRegistrationRequestDto finishRegistrationRequestDto) {

        requestService.forwardRequest(
                dealUrl,
                DealOperation.FINISH_REGISTRATION.getPath().replace("*", statementId),
                HttpMethod.POST,
                Optional.of(finishRegistrationRequestDto),
                Void.class
        );
    }

    public void requestSendDocuments(String statementId) {
        requestService.forwardRequest(
                dealUrl,
                DealOperation.REQUEST_DOCUMENTS.getPath().replace("*", statementId),
                HttpMethod.POST,
                Optional.empty(),
                Void.class
        );
    }

    public void requestSignDocuments(String statementId) {
        requestService.forwardRequest(
                dealUrl,
                DealOperation.REQUEST_SIGN.getPath().replace("*", statementId),
                HttpMethod.POST,
                Optional.empty(),
                Void.class
        );
    }

    public void codeSignDocuments(String statementId, String sesCode) {
        requestService.forwardRequest(
                dealUrl,
                DealOperation.CODE_SIGN.getPath().replace("*", statementId),
                HttpMethod.POST,
                Optional.of(sesCode),
                Void.class
        );
    }

    public Statement getStatementById(String statementId) {
        return requestService.forwardRequest(
                dealUrl,
                DealOperation.GET_STATEMENT_BY_ID.getPath().replace("*", statementId),
                HttpMethod.GET,
                Optional.empty(),
                Statement.class
        );
    }

    public List<Statement> getAllStatements() {
        return Arrays.asList(requestService.forwardRequest(
                dealUrl,
                DealOperation.GET_ALL_STATEMENTS.getPath(),
                HttpMethod.GET,
                Optional.empty(),
                Statement[].class)
        );
    }
}
