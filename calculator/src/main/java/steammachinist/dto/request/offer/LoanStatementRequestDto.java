package steammachinist.dto.request.offer;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Loan request data")
public record LoanStatementRequestDto(
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
        @Schema(description = "User email", example = "ivanov_i_1985@example.com")
        String email,
        @Schema(description = "User date of birth", example = "1985-05-12")
        LocalDate birthdate,
        @Schema(description = "User passport series", example = "2017")
        String passportSeries,
        @Schema(description = "User passport number", example = "927632")
        String passportNumber
) {
}
