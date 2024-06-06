package steammachinist.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import steammachinist.dto.scoring.request.*;
import steammachinist.dto.scoring.response.CreditDto;
import steammachinist.dto.scoring.response.PaymentScheduleElementDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor
public class CalculatorServiceTest {

    @Autowired
    private CalculatorService calculatorService;

    private static ScoringDataDto scoringDataDto;
    private static CreditDto creditDtoCorrect;

    @BeforeAll
    static void setUpDtos() {
        scoringDataDto = ScoringDataDto.builder()
                .firstName("Name")
                .lastName("Name")
                .middleName("Name")
                .amount(BigDecimal.valueOf(1000000))
                .term(6)
                .birthdate(LocalDate.now().minusYears(40))
                .passportSeries("2020")
                .passportNumber("123456")
                .gender(Gender.MALE)
                .passportIssueDate(LocalDate.now().minusYears(5))
                .passportIssueBranch("Отделение мвд")
                .accountNumber("123123123")
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .dependentAmount(1)
                .maritalStatus(MaritalStatus.MARRIED)
                .employment(EmploymentDto.builder()
                        .employerINN("123123")
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .position(Position.MID_MANAGER)
                        .salary(BigDecimal.valueOf(250000))
                        .workExperienceCurrent(40)
                        .workExperienceTotal(240)
                        .build())
                .build();

        creditDtoCorrect = CreditDto.builder()
                .amount(scoringDataDto.amount())
                .term(scoringDataDto.term())
                .rate(BigDecimal.valueOf(7.0))
                .psk(BigDecimal.valueOf(1020515.64))
                .monthlyPayment(BigDecimal.valueOf(170085.94))
                .isInsuranceEnabled(scoringDataDto.isInsuranceEnabled())
                .isSalaryClient(scoringDataDto.isSalaryClient())
                .paymentSchedule(List.of(
                        PaymentScheduleElementDto.builder()
                                .number(0)
                                .date(LocalDate.now().plusMonths(1))
                                .totalPayment(BigDecimal.valueOf(170085.94))
                                .interestPayment(BigDecimal.valueOf(5833.33))
                                .debtPayment(BigDecimal.valueOf(164252.61))
                                .remainingDebt(BigDecimal.valueOf(835747.39))
                                .build(),
                        PaymentScheduleElementDto.builder()
                                .number(1)
                                .date(LocalDate.now().plusMonths(2))
                                .totalPayment(BigDecimal.valueOf(170085.94))
                                .interestPayment(BigDecimal.valueOf(4875.19))
                                .debtPayment(BigDecimal.valueOf(165210.75))
                                .remainingDebt(BigDecimal.valueOf(670536.64))
                                .build(),
                        PaymentScheduleElementDto.builder()
                                .number(2)
                                .date(LocalDate.now().plusMonths(3))
                                .totalPayment(BigDecimal.valueOf(170085.94))
                                .interestPayment(BigDecimal.valueOf(3911.46))
                                .debtPayment(BigDecimal.valueOf(166174.48))
                                .remainingDebt(BigDecimal.valueOf(504362.16))
                                .build(),
                        PaymentScheduleElementDto.builder()
                                .number(3)
                                .date(LocalDate.now().plusMonths(4))
                                .totalPayment(BigDecimal.valueOf(170085.94))
                                .interestPayment(BigDecimal.valueOf(2942.11))
                                .debtPayment(BigDecimal.valueOf(167143.83))
                                .remainingDebt(BigDecimal.valueOf(337218.33))
                                .build(),
                        PaymentScheduleElementDto.builder()
                                .number(4)
                                .date(LocalDate.now().plusMonths(5))
                                .totalPayment(BigDecimal.valueOf(170085.94))
                                .interestPayment(BigDecimal.valueOf(1967.11))
                                .debtPayment(BigDecimal.valueOf(168118.83))
                                .remainingDebt(BigDecimal.valueOf(169099.50).setScale(2))
                                .build(),
                        PaymentScheduleElementDto.builder()
                                .number(5)
                                .date(LocalDate.now().plusMonths(6))
                                .totalPayment(BigDecimal.valueOf(170085.94))
                                .interestPayment(BigDecimal.valueOf(986.41))
                                .debtPayment(BigDecimal.valueOf(169099.53))
                                .remainingDebt(BigDecimal.valueOf(-0.03))
                                .build()
                ))
                .build();
    }

    @Test
    public void testCalculateCredit() {
        assertEquals(creditDtoCorrect, calculatorService.scoreAndCalculateCredit(scoringDataDto));
    }
}
