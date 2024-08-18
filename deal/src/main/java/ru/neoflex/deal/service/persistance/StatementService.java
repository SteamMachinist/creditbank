package ru.neoflex.deal.service.persistance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.common.dto.statement.StatementDto;
import ru.neoflex.deal.entity.Statement;
import ru.neoflex.deal.mapper.StatementMapper;
import ru.neoflex.deal.repository.StatementRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatementService {

    private final StatementRepository statementRepository;
    private final StatementMapper statementMapper;

    private Statement findByIdOrThrow(UUID id) {
        return statementRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Statement with id %s not found", id))
        );
    }

    public Statement getStatementById(UUID id) {
        return findByIdOrThrow(id);
    }

    public List<Statement> getAllStatements() {
        return statementRepository.findAll();
    }

    public StatementDto getStatementDtoById(UUID id) {
        return statementMapper.map(findByIdOrThrow(id));
    }

    public List<StatementDto> getAllStatementDtos() {
        return statementRepository.findAll().stream().map(statementMapper::map).collect(Collectors.toList());
    }

    public Statement addStatement(Statement statement) {
        return statementRepository.save(statement);
    }

    public Statement updateStatement(Statement statement) {
        return statementRepository.save(statement);
    }

    public void deleteStatementById(UUID id) {
        statementRepository.delete(findByIdOrThrow(id));
    }
}
