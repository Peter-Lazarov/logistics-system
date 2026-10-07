package com.sunny.times.common.clients;

import com.sunny.times.common.exception.ClientNotFoundException;
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
        return repository.findAll().stream().map(ClientMapper::toDto).toList();
    }

    public ClientDto getById(Long clientUserId) {
        ClientEntity entity = repository.findById(clientUserId).orElseThrow(() -> new ClientNotFoundException(clientUserId));
        return ClientMapper.toDto(entity);
    }

    public ClientDto create(ClientDto dto) {

        ClientEntity entity = new ClientEntity(dto.userId(), dto.companyName(), dto.companyAddress(), dto.vatNumber());

        return ClientMapper.toDto(repository.save(entity));
    }

}
