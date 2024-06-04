package steammachinist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import steammachinist.dto.scoring.response.CreditDto;
import steammachinist.dto.offer.response.LoanOfferDto;
import steammachinist.dto.offer.request.LoanStatementRequestDto;
import steammachinist.dto.scoring.request.ScoringDataDto;

import java.util.List;

@Tag(
        name = "Calculator MS",
        description = "MS for calculation of possible loan offers and full credit info"
)
public interface CalculatorApi {

    @Operation(
            summary = "Generate loan offers from request",
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
                            description = "Successful loan offers generation",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = LoanOfferDto.class)))
                            }
                    )
            }
    )
    List<LoanOfferDto> generatePossibleLoanOffers(LoanStatementRequestDto loanStatementRequestDto);

    @Operation(
            summary = "Conduct scoring and calculate full credit info",
            description = "Conducts validation and scoring of provided scoring data, "
                    + "calculates full credit information",
            requestBody = @RequestBody(
                    description = "Data for scoring and credit calculation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ScoringDataDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful scoring and credit data calculation",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CreditDto.class))
                            }
                    )
            }
    )
    CreditDto calculateFullCreditData(ScoringDataDto scoringDataDto);
}
