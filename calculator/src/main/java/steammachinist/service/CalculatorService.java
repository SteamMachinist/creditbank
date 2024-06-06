package steammachinist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import steammachinist.dto.GeneralCreditInfo;
import steammachinist.dto.exception.CreditDeniedException;
import steammachinist.dto.offer.request.LoanStatementRequestDto;
import steammachinist.dto.offer.response.LoanOfferDto;
import steammachinist.dto.scoring.request.EmploymentDto;
import steammachinist.dto.scoring.request.Gender;
import steammachinist.dto.scoring.request.ScoringDataDto;
import steammachinist.dto.scoring.response.CreditDto;
import steammachinist.dto.scoring.response.PaymentScheduleElementDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static steammachinist.util.CalculatorUtil.generateBooleanCombinations;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalculatorService {

    private static final int POSSIBLE_OFFERS_NUMBER = 4;

    @Value("${calculator.base-rate}")
    private BigDecimal BASE_RATE;

    private static final BigDecimal INSURANCE_RATE_CHANGE = BigDecimal.valueOf(-3);
    private static final BigDecimal SALARY_CLIENT_RATE_CHANGE = BigDecimal.valueOf(-1);
    private static final BigDecimal SELF_EMPLOYED_RATE_CHANGE = BigDecimal.valueOf(1);
    private static final BigDecimal BUSINESS_OWNER_RATE_CHANGE = BigDecimal.valueOf(2);
    private static final BigDecimal MID_MANAGER_RATE_CHANGE = BigDecimal.valueOf(-2);
    private static final BigDecimal TOP_MANAGER_RATE_CHANGE = BigDecimal.valueOf(-3);
    private static final BigDecimal MARRIED_RATE_CHANGE = BigDecimal.valueOf(-3);
    private static final BigDecimal DIVORCED_RATE_CHANGE = BigDecimal.valueOf(1);
    private static final BigDecimal PREFERRED_MALE_AGE_RATE_CHANGE = BigDecimal.valueOf(-3);
    private static final BigDecimal PREFERRED_FEMALE_AGE_RATE_CHANGE = BigDecimal.valueOf(-3);
    private static final BigDecimal NON_BINARY_RATE_CHANGE = BigDecimal.valueOf(7);

    private static final int MIN_CURRENT_EXPERIENCE = 3;
    private static final int MIN_TOTAL_EXPERIENCE = 18;

    private static final int MIN_AGE_BOUND = 20;
    private static final int MAX_AGE_BOUND = 65;

    private static final int PREFERRED_MALE_AGE_MIN = 30;
    private static final int PREFERRED_MALE_AGE_MAX = 55;
    private static final int PREFERRED_FEMALE_AGE_MIN = 32;
    private static final int PREFERRED_FEMALE_AGE_MAX = 60;

    private static final BigDecimal MAX_AMOUNT_IN_SALARIES = BigDecimal.valueOf(25);

    private static final BigDecimal INSURANCE_PROPORTION = BigDecimal.valueOf(0.1);

    private static final BigDecimal MONTH_IN_YEAR = BigDecimal.valueOf(12);

    public List<LoanOfferDto> prescoreAndGenerateOffers(LoanStatementRequestDto loanStatementRequestDto) {
        return generateIncompletePossibleOffers(loanStatementRequestDto)
                .stream()
                .map(this::calculateOfferRate)
                .map(this::calculateOfferMonthlyPayment)
                .map(this::calculateOfferTotalAmount)
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

    private LoanOfferDto calculateOfferRate(LoanOfferDto loanOfferDto) {
        return loanOfferDto.withRate(calculateBasicChangedRate(loanOfferDto));
    }

    private LoanOfferDto calculateOfferMonthlyPayment(LoanOfferDto loanOfferDto) {
        return loanOfferDto.withMonthlyPayment(calculateBasicMonthlyPayment(loanOfferDto));
    }

    private LoanOfferDto calculateOfferTotalAmount(LoanOfferDto loanOfferDto) {
        return loanOfferDto.withTotalAmount(calculateBasicTotalAmount(loanOfferDto));
    }

    public CreditDto scoreAndCalculateCredit(ScoringDataDto scoringDataDto) {
        CreditDto creditDto = generateInitialCredit(scoringDataDto);
        creditDto = scoreCreditRate(creditDto, scoringDataDto);
        creditDto = calculateCreditMonthlyPayment(creditDto);
        creditDto = calculateCreditPsk(creditDto);
        creditDto = calculateCreditPaymentSchedule(creditDto);
        return creditDto;
    }

    public CreditDto generateInitialCredit(ScoringDataDto scoringDataDto) {
        return CreditDto.builder()
                .amount(scoringDataDto.amount())
                .term(scoringDataDto.term())
                .isInsuranceEnabled(scoringDataDto.isInsuranceEnabled())
                .isSalaryClient(scoringDataDto.isSalaryClient())
                .build();
    }

    private CreditDto scoreCreditRate(CreditDto creditDto, ScoringDataDto scoringDataDto) {
        BigDecimal rate = calculateBasicChangedRate(creditDto);
        rate = scoreEmployment(rate, creditDto, scoringDataDto);
        rate = scoreMaritalStatus(rate, scoringDataDto);
        rate = scoreGenderAndAge(rate, scoringDataDto);
        return creditDto.withRate(rate);
    }

    private BigDecimal scoreEmployment(BigDecimal rate, CreditDto creditDto, ScoringDataDto scoringDataDto) {
        EmploymentDto employmentDto = scoringDataDto.employment();
        rate = switch (employmentDto.employmentStatus()) {
            case UNEMPLOYED -> throw new CreditDeniedException("");
            case EMPLOYED -> rate;
            case SELF_EMPLOYED -> rate.add(SELF_EMPLOYED_RATE_CHANGE);
            case BUSINESS_OWNER -> rate.add(BUSINESS_OWNER_RATE_CHANGE);
        };
        rate = switch (employmentDto.position()) {
            case WORKER -> rate;
            case MID_MANAGER -> rate.add(MID_MANAGER_RATE_CHANGE);
            case TOP_MANAGER -> rate.add(TOP_MANAGER_RATE_CHANGE);
            case OWNER -> rate;
        };
        if (creditDto.amount().compareTo(
                employmentDto.salary().multiply(MAX_AMOUNT_IN_SALARIES)) > 0) {
            throw new CreditDeniedException("");
        }
        if (employmentDto.workExperienceCurrent() < MIN_CURRENT_EXPERIENCE
                || employmentDto.workExperienceTotal() < MIN_TOTAL_EXPERIENCE) {
            throw new CreditDeniedException("");
        }
        return rate;
    }

    private BigDecimal scoreMaritalStatus(BigDecimal rate, ScoringDataDto scoringDataDto) {
        return switch (scoringDataDto.maritalStatus()) {
            case MARRIED -> rate.add(MARRIED_RATE_CHANGE);
            case DIVORCED -> rate.add(DIVORCED_RATE_CHANGE);
            case SINGLE -> rate;
            case WIDOW_WIDOWER -> rate;
        };
    }

    private BigDecimal scoreGenderAndAge(BigDecimal rate, ScoringDataDto scoringDataDto) {
        int age = Period.between(scoringDataDto.birthdate(), LocalDate.now()).getYears();
        Gender gender = scoringDataDto.gender();
        if (age < MIN_AGE_BOUND || age > MAX_AGE_BOUND) {
            throw new CreditDeniedException("");
        }
        rate = switch (gender) {
            case MALE -> {
                if (age > PREFERRED_MALE_AGE_MIN && age < PREFERRED_MALE_AGE_MAX) {
                    yield rate.add(PREFERRED_MALE_AGE_RATE_CHANGE);
                } else yield rate;
            }
            case FEMALE -> {
                if (age > PREFERRED_FEMALE_AGE_MIN && age < PREFERRED_FEMALE_AGE_MAX) {
                    yield rate.add(PREFERRED_FEMALE_AGE_RATE_CHANGE);
                } else yield rate;
            }
            case NON_BINARY -> rate.add(NON_BINARY_RATE_CHANGE);
        };
        return rate;
    }

    private CreditDto calculateCreditMonthlyPayment(CreditDto creditDto) {
        return creditDto.withMonthlyPayment(calculateBasicMonthlyPayment(creditDto));
    }

    private CreditDto calculateCreditPsk(CreditDto creditDto) {
        return creditDto.withPsk(calculateBasicTotalAmount(creditDto));
    }

    private CreditDto calculateCreditPaymentSchedule(CreditDto creditDto) {
        BigDecimal monthlyRate = calculateMonthlyRate(creditDto.rate());
        BigDecimal interestPayment = calculateInterestPayment(creditDto.amount(), monthlyRate);
        BigDecimal debtPayment = creditDto.monthlyPayment().subtract(interestPayment);

        List<PaymentScheduleElementDto> paymentSchedule = new ArrayList<>();
        paymentSchedule.add(
                PaymentScheduleElementDto.builder()
                        .number(0)
                        .date(LocalDate.now().plusMonths(1))
                        .totalPayment(creditDto.monthlyPayment())
                        .debtPayment(debtPayment)
                        .interestPayment(interestPayment)
                        .remainingDebt(creditDto.amount().subtract(debtPayment))
                        .build());

        for (int i = 1; i < creditDto.term(); i++) {
            paymentSchedule.add(calculateNextPayment(paymentSchedule.get(i - 1), monthlyRate));
        }
        return creditDto.withPaymentSchedule(paymentSchedule);
    }

    private PaymentScheduleElementDto calculateNextPayment(PaymentScheduleElementDto previous, BigDecimal monthlyRate) {
        BigDecimal interestPayment = calculateInterestPayment(previous.remainingDebt(), monthlyRate);
        BigDecimal debtPayment = previous.totalPayment().subtract(interestPayment);
        return PaymentScheduleElementDto.builder()
                .number(previous.number() + 1)
                .date(previous.date().plusMonths(1))
                .totalPayment(previous.totalPayment())
                .interestPayment(interestPayment)
                .debtPayment(debtPayment)
                .remainingDebt(previous.remainingDebt().subtract(debtPayment))
                .build();
    }

    private BigDecimal calculateInterestPayment(BigDecimal remainingDebt, BigDecimal monthlyRate) {
        return remainingDebt.multiply(monthlyRate).setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calculateBasicChangedRate(GeneralCreditInfo generalCreditInfo) {
        BigDecimal rate = BASE_RATE;
        if (generalCreditInfo.isInsuranceEnabled()) {
            rate = rate.add(INSURANCE_RATE_CHANGE);
        }
        if (generalCreditInfo.isSalaryClient()) {
            rate = rate.add(SALARY_CLIENT_RATE_CHANGE);
        }
        return rate;
    }

    private BigDecimal calculateBasicMonthlyPayment(GeneralCreditInfo generalCreditInfo) {

        BigDecimal amount = generalCreditInfo.amount();
        if (generalCreditInfo.isInsuranceEnabled()) {
            amount = amount.add(generalCreditInfo.amount().multiply(INSURANCE_PROPORTION));
        }
        return calculateMonthlyPayment(amount, generalCreditInfo.rate(), generalCreditInfo.term());
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term) {

        BigDecimal monthlyRate = calculateMonthlyRate(rate);
        BigDecimal onePlusRateToPowTerm = BigDecimal.ONE.add(monthlyRate).pow(term);

        BigDecimal annuityCoefficient = monthlyRate.multiply(onePlusRateToPowTerm)
                .divide(onePlusRateToPowTerm.subtract(BigDecimal.ONE), 32, RoundingMode.HALF_EVEN);

        return amount.multiply(annuityCoefficient).setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calculateMonthlyRate(BigDecimal rate) {
        return rate
                .divide(BigDecimal.valueOf(100), 32, RoundingMode.HALF_EVEN)
                .divide(MONTH_IN_YEAR, 32, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calculateBasicTotalAmount(GeneralCreditInfo generalCreditInfo) {
        return generalCreditInfo.monthlyPayment().multiply(BigDecimal.valueOf(generalCreditInfo.term()));
    }
}
