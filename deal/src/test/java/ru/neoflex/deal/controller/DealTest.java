package ru.neoflex.deal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.neoflex.calculator.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;
import ru.neoflex.calculator.dto.scoring.request.*;
import ru.neoflex.deal.dto.finishregistration.request.FinishRegistrationRequestDto;
import ru.neoflex.deal.entity.ApplicationStatus;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.entity.Credit;
import ru.neoflex.deal.entity.Statement;
import ru.neoflex.deal.service.persistance.StatementService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class DealTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StatementService statementService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static LoanStatementRequestDto okLoanStatementRequestDto;
    private static FinishRegistrationRequestDto okFinishRegistrationRequestDto;
    private static FinishRegistrationRequestDto badRequestFinishRegistrationRequestDto;

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

        okFinishRegistrationRequestDto = FinishRegistrationRequestDto.builder()
                .gender(Gender.FEMALE)
                .passportIssueDate(LocalDate.now().minusYears(5))
                .passportIssueBranch("Отделение мвд")
                .accountNumber("123123123")
                .dependentAmount(1)
                .maritalStatus(MaritalStatus.MARRIED)
                .employment(EmploymentDto.builder()
                        .employerINN("123123")
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .position(Position.MID_MANAGER)
                        .salary(BigDecimal.valueOf(50000))
                        .workExperienceCurrent(24)
                        .workExperienceTotal(48)
                        .build())
                .build();
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @AfterEach
    void cleanup() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "statement", "client", "credit");
    }

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    public void testCreditApproved() throws Exception {
        MvcResult result = this.mockMvc.perform(
                        post(
                                "/deal/statement")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(okLoanStatementRequestDto)))
                .andExpect(status().isOk()).andReturn();

        LoanOfferDto[] loanOffers = objectMapper.readValue(result.getResponse().getContentAsString(), LoanOfferDto[].class);
        assertEquals(4, loanOffers.length);

        Statement statement = statementService.getStatementById(loanOffers[0].statementId());
        assertEquals(ApplicationStatus.PREAPPROVAL, statement.getStatus());

        Client client = statement.getClient();
        assertEquals(okLoanStatementRequestDto.firstName(), client.getFirstName());
        assertEquals(okLoanStatementRequestDto.lastName(), client.getLastName());
        assertEquals(okLoanStatementRequestDto.middleName(), client.getMiddleName());
        assertEquals(okLoanStatementRequestDto.email(), client.getEmail());
        assertEquals(okLoanStatementRequestDto.birthDate(), client.getBirthDate());
        assertEquals(okLoanStatementRequestDto.passportNumber(), client.getPassport().number());
        assertEquals(okLoanStatementRequestDto.passportSeries(), client.getPassport().series());


        this.mockMvc.perform(
                        post(
                                "/deal/offer/select")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loanOffers[0])))
                .andExpect(status().isOk());

        statement = statementService.getStatementById(loanOffers[0].statementId());
        assertEquals(ApplicationStatus.APPROVED, statement.getStatus());
        assertEquals(loanOffers[0], statement.getAppliedOffer());


        this.mockMvc.perform(
                        post(
                                "/deal/calculate/" + loanOffers[0].statementId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(okFinishRegistrationRequestDto)))
                .andExpect(status().isOk());

        statement = statementService.getStatementById(loanOffers[0].statementId());
        assertEquals(ApplicationStatus.CC_APPROVED, statement.getStatus());

        client = statement.getClient();
        assertEquals(okFinishRegistrationRequestDto.employment().employerINN(), client.getEmployment().employerINN());
        assertEquals(okFinishRegistrationRequestDto.employment().employmentStatus(), client.getEmployment().employmentStatus());
        assertEquals(okFinishRegistrationRequestDto.employment().position(), client.getEmployment().position());
        assertEquals(okFinishRegistrationRequestDto.employment().salary(), client.getEmployment().salary());
        assertEquals(okFinishRegistrationRequestDto.employment().workExperienceCurrent(), client.getEmployment().workExperienceCurrent());
        assertEquals(okFinishRegistrationRequestDto.employment().workExperienceTotal(), client.getEmployment().workExperienceTotal());
        assertEquals(okFinishRegistrationRequestDto.maritalStatus(), client.getMaritalStatus());
        assertEquals(okFinishRegistrationRequestDto.gender(), client.getGender());
        assertEquals(okFinishRegistrationRequestDto.dependentAmount(), client.getDependentAmount());
        assertEquals(okFinishRegistrationRequestDto.passportIssueBranch(), client.getPassport().issueBranch());
        assertEquals(okFinishRegistrationRequestDto.passportIssueDate(), client.getPassport().issueDate());
        assertEquals(okFinishRegistrationRequestDto.accountNumber(), client.getAccountNumber());

        Credit credit = statement.getCredit();
    }

    @Test
    public void testCreditDenied() throws Exception {
        MvcResult result = this.mockMvc.perform(
                        post(
                                "/deal/statement")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(okLoanStatementRequestDto)))
                .andExpect(status().isOk()).andReturn();

        LoanOfferDto loanOffer = objectMapper.readValue(result.getResponse().getContentAsString(), LoanOfferDto[].class)[0];

        this.mockMvc.perform(
                        post(
                                "/deal/offer/select")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loanOffer)))
                .andExpect(status().isOk());

        this.mockMvc.perform(
                        post(
                                "/deal/calculate/" + loanOffer.statementId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(badRequestFinishRegistrationRequestDto)))
                .andExpect(status().isBadRequest());
    }
}
