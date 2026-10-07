package com.sunny.times.contracts.clients;

public record ClientDto(
        Long userId,
        String companyName,
        String companyAddress,
        String vatNumber
) {}
