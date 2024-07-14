package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.calculator.dto.scoring.request.ScoringDataDto;
import ru.neoflex.deal.entity.Statement;

@Mapper(componentModel = "spring")
public interface ScoringDataMapper {

    @Mapping(target = ".", source = "statement.client")
    @Mapping(target = "birthdate", source = "statement.client.birthDate")
    @Mapping(target = "passportNumber", source = "statement.client.passport.number")
    @Mapping(target = "passportSeries", source = "statement.client.passport.series")
    @Mapping(target = "passportIssueBranch", source = "statement.client.passport.issueBranch")
    @Mapping(target = "passportIssueDate", source = "statement.client.passport.issueDate")
    @Mapping(target = ".", source = "statement.appliedOffer")
    @Mapping(target = "amount", source = "statement.appliedOffer.requestedAmount")
    ScoringDataDto map(Statement statement);
}
