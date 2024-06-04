package steammachinist.dto.scoring.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "User employment data")
public record EmploymentDto(

        @Schema(description = "User employment status", example = "EMPLOYED")
        EmploymentStatus employmentStatus,
        @Schema(description = "Employer INN", example = "123123123")
        String employerINN,
        @Schema(description = "User salary", example = "110000")
        BigDecimal salary,
        @Schema(description = "User employment position", example = "MID_MANAGER")
        Position position,
        @Schema(description = "User total work experience", example = "240")
        Integer workExperienceTotal,
        @Schema(description = "User current work experience", example = "40")
        Integer workExperienceCurrent
) {
}
