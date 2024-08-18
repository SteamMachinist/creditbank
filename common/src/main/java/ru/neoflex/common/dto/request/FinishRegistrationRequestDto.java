package ru.neoflex.common.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import ru.neoflex.common.dto.scoring.request.EmploymentDto;
import ru.neoflex.common.dto.scoring.request.Gender;
import ru.neoflex.common.dto.scoring.request.MaritalStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Schema(description = "Data for finishing registration")
@Builder
public record FinishRegistrationRequestDto(

        @NotNull
        @Schema(description = "User gender", example = "MALE")
        Gender gender,

        @NotNull
        @Schema(description = "User marital status", example = "MARRIED")
        MaritalStatus maritalStatus,

        @NotNull
        @Schema(description = "User number of dependent family members", example = "1")
        Integer dependentAmount,

        @NotNull
        @Schema(description = "User passport issue date", example = "2013-06-07")
        LocalDate passportIssueDate,

        @NotBlank
        @Schema(description = "User passport issue branch", example = "УМВД РОССИИ ПО ОРЛОВСКОЙ ОБЛ.")
        String passportIssueBranch,

        @NotNull
        @Schema(description = "User employment info")
        EmploymentDto employment,

        @NotBlank
        @Pattern(regexp = "[0-9]+")
        @Schema(description = "User account number", example = "123123123")
        String accountNumber
) {
}
