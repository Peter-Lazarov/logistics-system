package com.sunny.times.common.clients;

import com.sunny.times.common.exception.ClientNotFoundException;
import com.sunny.times.contracts.clients.ClientDto;
import com.sunny.times.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository repository;
    private final CurrentUserService currentUserService;

    public ClientService(
            ClientRepository repository,
            CurrentUserService currentUserService
    ) {
        this.repository = repository;
        this.currentUserService = currentUserService;
    }

    public List<ClientDto> getAll() {
        return repository.findAll().stream().map(ClientMapper::toDto).toList();
    }

    public ClientDto getById(Long clientUserId) {
        ClientEntity entity = repository.findById(clientUserId).orElseThrow(() -> new ClientNotFoundException(clientUserId));
        return ClientMapper.toDto(entity);
    }

    public ClientDto getCurrent() {
        Long currentUserId = currentUserService.getCurrentUser().userId();
        return getById(currentUserId);
    }

    public ClientDto create(ClientDto dto) {
        ClientEntity entity = new ClientEntity(dto.userId(), dto.companyName(), dto.companyAddress(), dto.vatNumber());
        return ClientMapper.toDto(repository.save(entity));
    }

    public ClientDto update(Long clientUserId, ClientDto dto) {
        ClientEntity entity = repository.findById(clientUserId).orElseThrow(() -> new ClientNotFoundException(clientUserId));
        entity.setCompanyName(dto.companyName());
        entity.setCompanyAddress(dto.companyAddress());
        entity.setVatNumber(dto.vatNumber());

        return ClientMapper.toDto(repository.save(entity));
    }

    public ClientDto updateCurrent(ClientDto dto) {
        Long currentUserId = currentUserService.getCurrentUser().userId();
        if (!dto.userId().equals(currentUserId)) {
            throw new IllegalArgumentException(
                    "User ID in request does not match authenticated user"
            );
        }
        return update(currentUserId, dto);
    }
}
