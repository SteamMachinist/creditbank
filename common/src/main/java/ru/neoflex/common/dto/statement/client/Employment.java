package ru.neoflex.common.dto.statement.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.With;
import ru.neoflex.common.dto.scoring.request.EmploymentStatus;
import ru.neoflex.common.dto.scoring.request.Position;

import java.math.BigDecimal;
import java.util.UUID;

public record Employment(

        @JsonProperty("employment_uuid")
        @With
        UUID employmentUUID,
        @JsonProperty("employment_status")
        EmploymentStatus employmentStatus,
        @JsonProperty("employer_inn")
        String employerINN,
        BigDecimal salary,
        Position position,
        @JsonProperty("work_experience_total")
        Integer workExperienceTotal,
        @JsonProperty("work_experience_current")
        Integer workExperienceCurrent) {
}
