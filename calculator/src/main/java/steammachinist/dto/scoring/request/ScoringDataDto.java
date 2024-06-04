package steammachinist.dto.scoring.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "User data for scoring")
public record ScoringDataDto(

        @Schema(description = "Requested loan amount", example = "1500000")
        BigDecimal amount,
        @Schema(description = "Requested loan term", example = "24")
        Integer term,
        @Schema(description = "User first name", example = "Иван")
        String firstName,
        @Schema(description = "User last name", example = "Иванов")
        String lastName,
        @Schema(description = "User middle name", example = "Иванович")
        String middleName,
        @Schema(description = "User gender", example = "MALE")
        Gender gender,
        @Schema(description = "User date of birth", example = "1985-05-12")
        LocalDate birthdate,
        @Schema(description = "User passport series", example = "2017")
        String passportSeries,
        @Schema(description = "User passport number", example = "927632")
        String passportNumber,
        @Schema(description = "User passport issue date", example = "2013-06-07")
        LocalDate passportIssueDate,
        @Schema(description = "User passport issue branch", example = "УМВД РОССИИ ПО ОРЛОВСКОЙ ОБЛ.")
        String passportIssueBranch,
        @Schema(description = "User marital status", example = "MARRIED")
        MaritalStatus maritalStatus,
        @Schema(description = "User number of dependent family members", example = "1")
        Integer dependentAmount,
        @Schema(description = "User employment info")
        EmploymentDto employment,
        @Schema(description = "User account number", example = "123123123")
        String accountNumber,
        @Schema(description = "Is loan insurance enabled", example = "false")
        Boolean isInsuranceEnabled,
        @Schema(description = "Is user a salary client", example = "false")
        Boolean isSalaryClient
) {
}
