package ru.neoflex.deal.entity;

import ru.neoflex.calculator.dto.scoring.request.EmploymentStatus;
import ru.neoflex.calculator.dto.scoring.request.Position;

import java.math.BigDecimal;

public record Employment(

        EmploymentStatus employmentStatus,
        String employerINN,
        BigDecimal salary,
        Position position,
        Integer workExperienceTotal,
        Integer workExperienceCurrent) {
}
