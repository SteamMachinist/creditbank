package ru.neoflex.statement;

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
import ru.neoflex.common.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.common.dto.offer.response.LoanOfferDto;
import ru.neoflex.statement.service.DealApiService;
import ru.neoflex.statement.service.StatementService;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeast;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@AutoConfigureMockMvc
public class StatementTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DealApiService dealApiService;

    @SpyBean
    private StatementService statementService;

    @Test
    public void testCreditApproved() throws Exception {
        LoanStatementRequestDto okLoanStatementRequestDto = LoanStatementRequestDto.builder().build();
        when(dealApiService.registerStatementAndGenerateOffers(any())).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(okLoanStatementRequestDto)))
                .andExpect(status().isOk());

        verify(statementService, atLeast(1)).prescoreAndGenerateLoanOffers(any());


        LoanOfferDto okLoanOfferDto = LoanOfferDto.builder().build();

        mockMvc.perform(post("/statement/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(okLoanOfferDto)))
                .andExpect(status().isOk());

        verify(statementService, atLeast(1)).chooseLoanOffer(any());
    }
}
