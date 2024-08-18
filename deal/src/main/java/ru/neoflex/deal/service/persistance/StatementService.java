package ru.neoflex.deal.service.persistance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.entity.Statement;
import ru.neoflex.deal.repository.StatementRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatementService {

    private final StatementRepository statementRepository;

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
