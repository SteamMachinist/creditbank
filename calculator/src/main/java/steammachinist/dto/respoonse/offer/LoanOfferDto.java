package steammachinist.dto.respoonse.offer;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Loan offer data")
public record LoanOfferDto(
        @Schema(description = "Unique offer UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID statementId,
        @Schema(description = "Requested loan amount", example = "1500000")
        BigDecimal requestedAmount,
        @Schema(description = "Total loan offer amount", example = "1815000")
        BigDecimal totalAmount,
        @Schema(description = "Loan offer term", example = "24")
        Integer term,
        @Schema(description = "Loan offer monthly payment", example = "75625")
        BigDecimal monthlyPayment,
        @Schema(description = "Loan offer rate", example = "10")
        BigDecimal rate,
        @Schema(description = "Loan insurance enabled", example = "false")
        Boolean isInsuranceEnabled,
        @Schema(description = "Salary client", example = "true")
        Boolean isSalaryClient
) {
}
