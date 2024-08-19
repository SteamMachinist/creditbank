package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import ru.neoflex.common.dto.scoring.response.CreditDto;
import ru.neoflex.deal.entity.Credit;

@Mapper(componentModel = "spring")
public interface CreditMapper {

    Credit map(CreditDto creditDto);
}
