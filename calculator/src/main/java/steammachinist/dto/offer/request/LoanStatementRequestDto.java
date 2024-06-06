package steammachinist.dto.offer.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import steammachinist.validation.Adult;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Loan request data")
@Builder
public record LoanStatementRequestDto(

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

        @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
        @Schema(description = "User email", example = "ivanov_i_1985@example.com")
        String email,

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
        String passportNumber
) {
}
