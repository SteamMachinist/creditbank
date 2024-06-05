package steammachinist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import steammachinist.dto.offer.request.LoanStatementRequestDto;
import steammachinist.dto.offer.response.LoanOfferDto;
import steammachinist.dto.scoring.request.ScoringDataDto;
import steammachinist.dto.scoring.response.CreditDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CalculatorService {

    private final int POSSIBLE_OFFERS_NUMBER = 4;
    @Value("${calculator.base-rate}")
    private BigDecimal BASE_RATE;
    private final BigDecimal INSURANCE_RATE_CHANGE = BigDecimal.valueOf(-3);
    private final BigDecimal SALARY_CLIENT_RATE_CHANGE = BigDecimal.valueOf(-1);
    private final BigDecimal INSURANCE_PROPORTION = BigDecimal.valueOf(0.1);
    private final BigDecimal MONTH_IN_YEAR = BigDecimal.valueOf(12);

    public List<LoanOfferDto> prescoreAndGenerateOffers(LoanStatementRequestDto loanStatementRequestDto) {
        return generateIncompletePossibleOffers(loanStatementRequestDto)
                .stream()
                .map(this::calculateOfferRate)
                .map(this::calculateOfferTotalAmountAndMonthlyPayment)
                .sorted(Comparator.comparing(LoanOfferDto::rate))
                .toList();
    }

    private List<LoanOfferDto> generateIncompletePossibleOffers(
            LoanStatementRequestDto loanStatementRequestDto) {

        BigDecimal requestedAmount = loanStatementRequestDto.amount();
        Integer term = loanStatementRequestDto.term();

        List<boolean[]> combinations = generateBooleanCombinations();

        return IntStream
                .range(0, POSSIBLE_OFFERS_NUMBER)
                .mapToObj(value -> {
                    boolean[] combination = combinations.get(value);
                    return LoanOfferDto.builder()
                            .statementId(UUID.randomUUID())
                            .requestedAmount(requestedAmount)
                            .term(term)
                            .isInsuranceEnabled(combination[0])
                            .isSalaryClient(combination[1])
                            .build();
                }).toList();
    }

    private List<boolean[]> generateBooleanCombinations() {
        List<boolean[]> combinations = new ArrayList<>();

        boolean[] values = {false, true};
        for (boolean first : values) {
            for (boolean second : values) {
                combinations.add(new boolean[]{first, second});
            }
        }
        return combinations;
    }

    private LoanOfferDto calculateOfferRate(LoanOfferDto loanOfferDto) {
        BigDecimal rate = BASE_RATE;
        if (loanOfferDto.isInsuranceEnabled()) {
            rate = rate.add(INSURANCE_RATE_CHANGE);
        }
        if (loanOfferDto.isSalaryClient()) {
            rate = rate.add(SALARY_CLIENT_RATE_CHANGE);
        }
        return loanOfferDto.withRate(rate);
    }

    private LoanOfferDto calculateOfferTotalAmountAndMonthlyPayment(
            LoanOfferDto loanOfferDto) {

        BigDecimal amount = loanOfferDto.requestedAmount();
        if (loanOfferDto.isInsuranceEnabled()) {
            amount = amount.add(loanOfferDto.requestedAmount().multiply(INSURANCE_PROPORTION));
        }

        BigDecimal monthlyPayment = calculateMonthlyPayment(amount, loanOfferDto.rate(), loanOfferDto.term());
        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(loanOfferDto.term()));

        return loanOfferDto.withTotalAmount(totalAmount).withMonthlyPayment(monthlyPayment);
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term) {
        BigDecimal monthlyRate = rate
                .setScale(32, RoundingMode.HALF_EVEN)
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_EVEN)
                .divide(MONTH_IN_YEAR, RoundingMode.HALF_EVEN);

        BigDecimal divider = BigDecimal.ONE.add(monthlyRate).pow(term).subtract(BigDecimal.ONE);

        BigDecimal annuityCoefficient = monthlyRate.add(
                monthlyRate.divide(divider, RoundingMode.HALF_EVEN));

        return amount.multiply(annuityCoefficient);
    }

    public CreditDto scoreAndCalculateCredit(ScoringDataDto scoringDataDto) {
        return null;
    }
}
