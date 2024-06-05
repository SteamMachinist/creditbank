package steammachinist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import steammachinist.dto.offer.request.LoanStatementRequestDto;
import steammachinist.dto.offer.response.LoanOfferDto;
import steammachinist.dto.scoring.response.CreditDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CalculatorService {

    private final int POSSIBLE_OFFERS_NUMBER = 4;
    @Value("${calculator.base-rate}")
    private BigDecimal BASE_RATE;
    private final BigDecimal INSURANCE_RATE_FACTOR = BigDecimal.valueOf(-3.0);
    private final BigDecimal SALARY_CLIENT_RATE_FACTOR = BigDecimal.valueOf(-1.0);

    public List<LoanOfferDto> prescoreAndGenerateOffers(LoanStatementRequestDto loanStatementRequestDto) {
        return generateIncompletePossibleOffers(loanStatementRequestDto)
                .stream()
                .map(this::calculateOfferRate)
                .map(this::calculateOfferTotalAmountAndMonthlyPayment)
                .toList();
    }

    private List<LoanOfferDto> generateIncompletePossibleOffers(
            LoanStatementRequestDto loanStatementRequestDto) {

        List<LoanOfferDto.LoanOfferDtoBuilder> offerBuilders
                = generateBasicOffers(loanStatementRequestDto, POSSIBLE_OFFERS_NUMBER);

        List<boolean[]> combinations = generateBooleanCombinations();


        return IntStream.range(0, POSSIBLE_OFFERS_NUMBER).mapToObj(value -> {
            boolean[] combination = combinations.get(value);
            return offerBuilders.get(value)
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

    private List<LoanOfferDto.LoanOfferDtoBuilder> generateBasicOffers(
            LoanStatementRequestDto loanStatementRequestDto, int number) {

        BigDecimal requestedAmount = loanStatementRequestDto.amount();
        Integer term = loanStatementRequestDto.term();
        return Stream.generate(
                        () -> LoanOfferDto.builder()
                                .statementId(UUID.randomUUID())
                                .requestedAmount(requestedAmount)
                                .term(term))
                .limit(number)
                .toList();
    }

    private LoanOfferDto calculateOfferRate(LoanOfferDto loanOfferDto) {
        BigDecimal rate = BASE_RATE;
        if(loanOfferDto.isInsuranceEnabled()) {
            rate = rate.add(INSURANCE_RATE_FACTOR);
        }
        return loanOfferDto;
    }

    private LoanOfferDto calculateOfferTotalAmountAndMonthlyPayment(
            LoanOfferDto loanOfferDto) {
        return loanOfferDto;
    }

    public CreditDto scoreAndCalculateCredit(LoanOfferDto loanOfferDto) {
        return null;
    }
}
