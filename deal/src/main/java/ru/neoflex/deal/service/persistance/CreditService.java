package ru.neoflex.deal.service.persistance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.entity.Credit;
import ru.neoflex.deal.repository.CreditRepository;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;

    private Credit findByIdOrThrow(UUID id) {
        return creditRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Credit with id %s not found", id))
        );
    }

    public Credit getCreditById(UUID id) {
        return findByIdOrThrow(id);
    }

    public Credit addCredit(Credit credit) {
        return creditRepository.save(credit);
    }

    public Credit updateCredit(Credit credit) {
        return creditRepository.save(credit);
    }

    public void deleteCreditById(UUID id) {
        creditRepository.delete(findByIdOrThrow(id));
    }
}
