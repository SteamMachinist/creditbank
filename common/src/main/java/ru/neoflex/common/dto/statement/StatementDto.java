package ru.neoflex.common.dto.statement;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.neoflex.common.dto.offer.response.LoanOfferDto;
import ru.neoflex.common.dto.statement.client.StatementClientDto;
import ru.neoflex.common.dto.statement.credit.StatementCreditDto;


import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public record StatementDto(

        @JsonProperty("statement_id")
        UUID statementId,
        StatementClientDto client,
        StatementCreditDto credit,
        ApplicationStatus status,
        @JsonProperty("creation_date")
        Timestamp creationDate,
        @JsonProperty("applied_offer")
        LoanOfferDto appliedOffer,
        @JsonProperty("sign_date")
        Timestamp signDate,
        @JsonProperty("ses_code")
        String sesCode,
        @JsonProperty("status_history")
        List<StatusHistoryElement> statusHistory
) {
}
