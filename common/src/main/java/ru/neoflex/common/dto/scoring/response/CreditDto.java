package ru.neoflex.common.dto.scoring.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.With;
import ru.neoflex.common.dto.GeneralCreditInfo;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Full credit data")
@Builder
public record CreditDto(

        @Schema(description = "Credit amount", example = "1500000")
        BigDecimal amount,

        @Schema(description = "Credit term", example = "24")
        Integer term,

        @With
        @Schema(description = "Credit monthly payment", example = "62500")
        BigDecimal monthlyPayment,

        @With
        @Schema(description = "Credit yearly rate", example = "10")
        BigDecimal rate,

        @With
        @Schema(description = "Full credit cost", example = "1815000")
        BigDecimal psk,

        @Schema(description = "Is credit insurance enabled", example = "false")
        Boolean isInsuranceEnabled,

        @Schema(description = "Is user a salary client", example = "false")
        Boolean isSalaryClient,

        @With
        @Schema(description = "List of credit payments")
        List<PaymentScheduleElementDto> paymentSchedule

) implements GeneralCreditInfo {
}
