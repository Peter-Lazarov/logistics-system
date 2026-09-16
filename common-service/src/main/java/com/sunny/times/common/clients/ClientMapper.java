package com.sunny.times.common.clients;

import com.sunny.times.contracts.clients.ClientDto;

public class ClientMapper {

    public static ClientDto toDto(ClientEntity entity) {
        return new ClientDto(
                entity.getClientId(),
                entity.getName(),
                entity.getPhone(),
                entity.getEmail()
        );
    }
}
