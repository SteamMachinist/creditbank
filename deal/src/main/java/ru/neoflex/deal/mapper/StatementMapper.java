package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import ru.neoflex.common.dto.statement.StatementDto;
import ru.neoflex.deal.entity.Statement;

@Mapper(componentModel = "spring")
public interface StatementMapper {

    StatementDto map(Statement statement);
}
