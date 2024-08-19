package ru.neoflex.common.dto.statement.credit;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.neoflex.common.dto.scoring.response.PaymentScheduleElementDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record StatementCreditDto(

        @JsonProperty("credit_id")
        UUID creditId,
        BigDecimal amount,
        Integer term,
        @JsonProperty("monthly_payment")
        BigDecimal monthlyPayment,
        BigDecimal rate,
        BigDecimal psk,
        @JsonProperty("payment_schedule")
        List<PaymentScheduleElementDto> paymentSchedule,
        @JsonProperty("is_insurance_enabled")
        Boolean isInsuranceEnabled,
        @JsonProperty("is_salary_client")
        Boolean isSalaryClient,
        @JsonProperty("credit_status")
        CreditStatus creditStatus) {
}
