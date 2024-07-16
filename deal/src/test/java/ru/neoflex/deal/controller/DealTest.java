package ru.neoflex.deal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.calculator.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;
import ru.neoflex.calculator.dto.scoring.response.CreditDto;
import ru.neoflex.deal.dto.finishregistration.request.FinishRegistrationRequestDto;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.entity.Passport;
import ru.neoflex.deal.entity.Statement;
import ru.neoflex.deal.repository.ClientRepository;
import ru.neoflex.deal.repository.CreditRepository;
import ru.neoflex.deal.repository.StatementRepository;
import ru.neoflex.deal.service.CalculatorApiService;
import ru.neoflex.deal.service.DealService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@AutoConfigureMockMvc
public class DealTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private CreditRepository creditRepository;
    @MockBean
    private StatementRepository statementRepository;

    @MockBean
    private CalculatorApiService calculatorApiService;

    @SpyBean
    private DealService dealService;

    @Test
    public void testCreditApproved() throws Exception {
        LoanStatementRequestDto okLoanStatementRequestDto = LoanStatementRequestDto.builder()
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

        when(calculatorApiService.getPossibleOffers(any())).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(okLoanStatementRequestDto)))
                .andExpect(status().isOk());

        verify(clientRepository, atLeast(1)).save(any());
        verify(statementRepository, atLeast(1)).save(any());

        UUID uuid = UUID.randomUUID();
        Client client = new Client();
        client.setPassport(Passport.builder().build());
        Statement statement = new Statement(client);
        statement.setStatementId(uuid);
        when(statementRepository.findById(any())).thenReturn(Optional.of(statement));

        mockMvc.perform(post("/deal/offer/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(LoanOfferDto.builder().statementId(uuid).build())))
                .andExpect(status().isOk());

        verify(clientRepository, atLeast(1)).save(any());
        verify(statementRepository, atLeast(1)).save(any());


        when(calculatorApiService.getFullCreditData(any())).thenReturn(CreditDto.builder().build());
        when(statementRepository.save(any())).thenReturn(statement);

        mockMvc.perform(post(String.format("/deal/calculate/%s", UUID.randomUUID()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(FinishRegistrationRequestDto.builder().build())))
                .andExpect(status().isOk());

        verify(clientRepository, atLeast(1)).save(any());
        verify(creditRepository, atLeast(1)).save(any());
        verify(statementRepository, atLeast(1)).save(any());
    }
}
