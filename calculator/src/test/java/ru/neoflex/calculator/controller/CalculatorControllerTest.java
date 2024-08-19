package ru.neoflex.calculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.common.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.common.dto.scoring.request.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static LoanStatementRequestDto okLoanStatementRequestDto;
    private static LoanStatementRequestDto badRequestLoanStatementRequestDto;

    private static ScoringDataDto okScoringDataDto;
    private static ScoringDataDto badRequestScoringDataDto;

    @BeforeAll
    static void setUpDtos() {
        okLoanStatementRequestDto = LoanStatementRequestDto.builder()
                .firstName("Name")
                .lastName("Name")
                .middleName("Name")
                .email("mail@mail.com")
                .amount(BigDecimal.valueOf(500000))
                .term(36)
                .birthDate(LocalDate.now().minusYears(30))
                .passportSeries("2020")
                .passportNumber("123456")
                .build();

        badRequestLoanStatementRequestDto = LoanStatementRequestDto.builder()
                .firstName("Name")
                .lastName("Name")
                .middleName("Name")
                .email("mail@mail.com")
                .amount(BigDecimal.valueOf(500000))
                .term(36)
                .birthDate(LocalDate.now().minusYears(16))
                .passportSeries("2020")
                .passportNumber("123456")
                .build();

        okScoringDataDto = ScoringDataDto.builder()
                .firstName("Name")
                .lastName("Name")
                .middleName("Name")
                .amount(BigDecimal.valueOf(500000))
                .term(36)
                .birthdate(LocalDate.now().minusYears(30))
                .passportSeries("2020")
                .passportNumber("123456")
                .gender(Gender.FEMALE)
                .passportIssueDate(LocalDate.now().minusYears(5))
                .passportIssueBranch("Отделение мвд")
                .accountNumber("123123123")
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .dependentAmount(1)
                .maritalStatus(MaritalStatus.MARRIED)
                .employment(EmploymentDto.builder()
                        .employerINN("123123")
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .position(Position.MID_MANAGER)
                        .salary(BigDecimal.valueOf(10000000))
                        .workExperienceCurrent(24)
                        .workExperienceTotal(48)
                        .build())
                .build();

        badRequestScoringDataDto = ScoringDataDto.builder()
                .firstName("Name")
                .lastName("Name")
                .middleName("Name")
                .amount(BigDecimal.valueOf(500000))
                .term(36)
                .birthdate(LocalDate.now().minusYears(30))
                .passportSeries("2020")
                .passportNumber("123456")
                .gender(Gender.FEMALE)
                .passportIssueDate(LocalDate.now().minusYears(5))
                .passportIssueBranch("Отделение мвд")
                .accountNumber("123123123")
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .dependentAmount(1)
                .maritalStatus(MaritalStatus.MARRIED)
                .employment(EmploymentDto.builder()
                        .employerINN("123123")
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .position(Position.MID_MANAGER)
                        .salary(BigDecimal.valueOf(500))
                        .workExperienceCurrent(24)
                        .workExperienceTotal(48)
                        .build())
                .build();
    }

    @Test
    public void testGenerateOffersOk() throws Exception {
        this.mockMvc.perform(
                post(
                        "/calculator/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(okLoanStatementRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGenerateOffersBadRequest() throws Exception {
        this.mockMvc.perform(
                        post(
                                "/calculator/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(badRequestLoanStatementRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGenerateCalculateCreditOk() throws Exception {
        this.mockMvc.perform(
                        post(
                                "/calculator/calc")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(okScoringDataDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGenerateCalculateCreditBadRequest() throws Exception {
        this.mockMvc.perform(
                        post(
                                "/calculator/calc")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(badRequestScoringDataDto)))
                .andExpect(status().isBadRequest());
    }
}
