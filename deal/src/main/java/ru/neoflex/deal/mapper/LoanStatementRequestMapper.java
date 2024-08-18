package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.common.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.common.dto.statement.client.Passport;

@Mapper(componentModel = "spring")
public interface LoanStatementRequestMapper {

    @Mapping(target = "series", source = "passportSeries")
    @Mapping(target = "number", source = "passportNumber")
    Passport mapPassport(LoanStatementRequestDto request);

    @Mapping(target = "passport", source = ".")
    Client mapClient(LoanStatementRequestDto request);
}
