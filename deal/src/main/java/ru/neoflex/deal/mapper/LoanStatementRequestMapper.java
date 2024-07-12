package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.calculator.dto.offer.request.LoanStatementRequestDto;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.entity.Passport;

@Mapper(componentModel = "spring", imports = {Passport.class})
public interface LoanStatementRequestMapper {

    @Mapping(target = "series", source = "passportSeries")
    @Mapping(target = "number", source = "passportNumber")
    Passport mapPassport(LoanStatementRequestDto request);

    @Mapping(target = "passport", source = ".")
    Client mapClient(LoanStatementRequestDto request);
}
