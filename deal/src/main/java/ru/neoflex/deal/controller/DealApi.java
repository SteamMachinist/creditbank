package ru.neoflex.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import ru.neoflex.calculator.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;
import ru.neoflex.deal.dto.finishregistration.request.FinishRegistrationRequestDto;

import java.util.List;

@Tag(
        name = "Deal MS",
        description = "MS for deal status handling and database persistence"
)
public interface DealApi {

    @Operation(
            summary = "Save new Client and Statement, generate loan offers from request",
            description = "Saves new Client and Statement to database, " +
                    "then generates a list of possible loan offers from provided loan request",
            requestBody = @RequestBody(
                    description = "Loan request data to create Client, Statement and generate offers for",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoanStatementRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Client and Statement creation, and loan offers generation",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = LoanOfferDto.class)))
                            }
                    )
            }
    )
    ResponseEntity<List<LoanOfferDto>> initialRegisterAndGenerateLoanOffers(
            LoanStatementRequestDto loanStatementRequestDto);

    @Operation(
            summary = "Chooses loan offer",
            description = "Sets provided in request loan offer as chosen, updates Statement status",
            requestBody = @RequestBody(
                    description = "Chosen loan offer",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoanOfferDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Loan offer chosen successfully",
                            content = @Content(schema = @Schema(implementation = Void.class))
                    )
            }
    )
    ResponseEntity<Void> chooseLoanOffer(LoanOfferDto loanOfferDto);

    @Operation(
            summary = "Finishes registration and fully calculates credit",
            description = "Generates a list of possible loan offers from provided loan request",
            requestBody = @RequestBody(
                    description = "Loan request data to generate offers for",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoanStatementRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Registration finished and credit calculated successfully",
                            content = @Content(schema = @Schema(implementation = Void.class))
                    )
            }
    )
    ResponseEntity<Void> finishRegistrationAndCalculateCredit(
            @Parameter(description = "Statement UUID") String statementId,
            FinishRegistrationRequestDto finishRegistrationRequestDto);
}
