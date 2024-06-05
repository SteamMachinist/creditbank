package steammachinist.dto.offer.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.With;
import steammachinist.dto.GeneralCreditInfo;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Loan offer data")
@Builder
public record LoanOfferDto(

        @Schema(description = "Unique offer UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID statementId,

        @Schema(description = "Requested loan amount", example = "1500000")
        BigDecimal requestedAmount,

        @With
        @Schema(description = "Total loan offer amount", example = "1815000")
        BigDecimal totalAmount,

        @Schema(description = "Loan offer term", example = "24")
        Integer term,

        @With
        @Schema(description = "Loan offer monthly payment", example = "75625")
        BigDecimal monthlyPayment,

        @With
        @Schema(description = "Loan offer rate", example = "10")
        BigDecimal rate,

        @Schema(description = "Loan insurance enabled", example = "false")
        Boolean isInsuranceEnabled,

        @Schema(description = "Salary client", example = "true")
        Boolean isSalaryClient
) implements GeneralCreditInfo {
}
