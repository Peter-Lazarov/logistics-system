package com.sunny.times.common.clients;

import com.sunny.times.contracts.clients.ClientDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public List<ClientDto> getAll() {
        return repository.findAll()
                .stream()
                .map(ClientMapper::toDto)
                .toList();
    }

    public ClientDto getById(String clientId) {
        ClientEntity entity = repository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found: " + clientId));
        return ClientMapper.toDto(entity);
    }
}
