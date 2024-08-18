package ru.neoflex.common.dto.scoring.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Data on individual payment")
@Builder
public record PaymentScheduleElementDto(

        @Schema(description = "Payment number", example = "1")
        Integer number,
        @Schema(description = "Payment date", example = "2024-07-10")
        LocalDate date,
        @Schema(description = "Total credit payment", example = "45000")
        BigDecimal totalPayment,
        @Schema(description = "Payment for credit interest", example = "15000")
        BigDecimal interestPayment,
        @Schema(description = "Payment for credit body", example = "30000")
        BigDecimal debtPayment,
        @Schema(description = "Remaining credit debt", example = "550000")
        BigDecimal remainingDebt
) {
}
