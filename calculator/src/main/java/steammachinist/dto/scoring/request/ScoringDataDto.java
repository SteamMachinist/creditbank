package steammachinist.dto.scoring.request;

import io.swagger.v3.oas.annotations.media.Schema;
import steammachinist.validation.Adult;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "User data for scoring")
public record ScoringDataDto(

        @NotNull
        @DecimalMin("30000")
        @Schema(description = "Requested loan amount", example = "1500000")
        BigDecimal amount,

        @NotNull
        @DecimalMin("6")
        @Schema(description = "Requested loan term", example = "24")
        Integer term,

        @NotBlank
        @Size(min = 2, max = 30)
        @Schema(description = "User first name", example = "Иван")
        String firstName,

        @NotBlank
        @Size(min = 2, max = 30)
        @Schema(description = "User last name", example = "Иванов")
        String lastName,

        @Size(min = 2, max = 30)
        @Schema(description = "User middle name", example = "Иванович")
        String middleName,

        @NotNull
        @Schema(description = "User gender", example = "MALE")
        Gender gender,

        @NotNull
        @Adult
        @Schema(description = "User date of birth", example = "1985-05-12")
        LocalDate birthdate,

        @NotNull
        @Size(min = 4, max = 4)
        @Pattern(regexp = "[0-9]+")
        @Schema(description = "User passport series", example = "2017")
        String passportSeries,

        @NotNull
        @Size(min = 6, max = 6)
        @Pattern(regexp = "[0-9]+")
        @Schema(description = "User passport number", example = "927632")
        String passportNumber,

        @NotNull
        @Schema(description = "User passport issue date", example = "2013-06-07")
        LocalDate passportIssueDate,

        @NotBlank
        @Schema(description = "User passport issue branch", example = "УМВД РОССИИ ПО ОРЛОВСКОЙ ОБЛ.")
        String passportIssueBranch,

        @NotNull
        @Schema(description = "User marital status", example = "MARRIED")
        MaritalStatus maritalStatus,

        @NotNull
        @Schema(description = "User number of dependent family members", example = "1")
        Integer dependentAmount,

        @NotNull
        @Schema(description = "User employment info")
        EmploymentDto employment,

        @NotBlank
        @Pattern(regexp = "[0-9]+")
        @Schema(description = "User account number", example = "123123123")
        String accountNumber,

        @NotNull
        @Schema(description = "Is loan insurance enabled", example = "false")
        Boolean isInsuranceEnabled,

        @NotNull
        @Schema(description = "Is user a salary client", example = "false")
        Boolean isSalaryClient
) {
}
