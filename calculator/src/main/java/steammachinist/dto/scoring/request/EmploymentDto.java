package steammachinist.dto.scoring.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Builder
@Schema(description = "User employment data")
public record EmploymentDto(

        @NotNull
        @Schema(description = "User employment status", example = "EMPLOYED")
        EmploymentStatus employmentStatus,

        @NotNull
        @Pattern(regexp = "[0-9]+")
        @Schema(description = "Employer INN", example = "123123123")
        String employerINN,

        @NotNull
        @Schema(description = "User salary", example = "110000")
        BigDecimal salary,

        @NotNull
        @Schema(description = "User employment position", example = "MID_MANAGER")
        Position position,

        @NotNull
        @Schema(description = "User total work experience", example = "240")
        Integer workExperienceTotal,

        @NotNull
        @Schema(description = "User current work experience", example = "40")
        Integer workExperienceCurrent
) {
}
