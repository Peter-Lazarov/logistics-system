package com.sunny.times.contracts.clients;

public record ClientDto(
        String clientId,
        String name,
        String phone,
        String email
) {}
