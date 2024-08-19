package ru.neoflex.gateway.controller.statement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import ru.neoflex.common.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.common.dto.offer.response.LoanOfferDto;

import java.util.List;

@Tag(
        name = "Statement",
        description = "Handles loan statement and loan offers"
)
public interface GatewayStatementApi {

    @Operation(
            summary = "Prescore Statement and generate loan offers from request",
            description = "Prescore loan statement request and generates a list of possible loan offers from provided loan request",
            requestBody = @RequestBody(
                    description = "Loan request data to generate offers from",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoanStatementRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful loan offers generation",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = LoanOfferDto.class)))
                            }
                    )
            }
    )
    ResponseEntity<List<LoanOfferDto>> prescoreAndGenerateLoanOffers(
            LoanStatementRequestDto loanStatementRequestDto);

    @Operation(
            summary = "Chooses loan offer",
            description = "Sets provided in request loan offer as chosen",
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
}
