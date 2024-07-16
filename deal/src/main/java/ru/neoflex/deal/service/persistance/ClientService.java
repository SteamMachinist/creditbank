package ru.neoflex.deal.service.persistance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.repository.ClientRepository;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    private Client findByIdOrThrow(UUID id) {
        return clientRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Client with id %s not found", id))
        );
    }

    public Client getClientById(UUID id) {
        return findByIdOrThrow(id);
    }

    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    public Client updateClient(Client client) {
        return clientRepository.save(client);
    }

    public void deleteClientById(UUID id) {
        clientRepository.delete(findByIdOrThrow(id));
    }
}
