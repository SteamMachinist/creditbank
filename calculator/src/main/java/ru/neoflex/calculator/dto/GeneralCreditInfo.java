package ru.neoflex.calculator.dto;

import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;
import ru.neoflex.calculator.dto.scoring.response.CreditDto;

import java.math.BigDecimal;

public interface GeneralCreditInfo {

    default BigDecimal amount() {
        if (this instanceof LoanOfferDto loanOffer) {
            return loanOffer.requestedAmount();
        } else if (this instanceof CreditDto credit) {
            return credit.amount();
        }
        throw new UnsupportedOperationException(String.format("Unsupported type: %s", this.getClass().getName()));
    }

    Integer term();

    BigDecimal monthlyPayment();
    GeneralCreditInfo withMonthlyPayment(BigDecimal monthlyPayment);

    BigDecimal rate();
    GeneralCreditInfo withRate(BigDecimal rate);

    Boolean isInsuranceEnabled();

    Boolean isSalaryClient();
}
