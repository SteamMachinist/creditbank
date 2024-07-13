package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.neoflex.deal.dto.finishregistration.request.FinishRegistrationRequestDto;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.entity.Employment;
import ru.neoflex.deal.entity.Passport;

@Mapper(componentModel = "spring")
public interface FinishRegistrationRequestMapper {

    @Mapping(target = "employment", source = ".")
    @Mapping(target = "passport", expression = "java(updatePassport(client.getPassport(), request))")
    void updateClient(@MappingTarget Client client, FinishRegistrationRequestDto request);

    default Passport updatePassport(Passport passport, FinishRegistrationRequestDto request) {
        return passport.withIssueBranchAndIssueDate(request.passportIssueBranch(), request.passportIssueDate());
    }

    @Mapping(target = ".", source = "employment")
    Employment mapEmployment(FinishRegistrationRequestDto request);
}
