package com.sunny.times.contracts.drivers;

public record DriverDto(
        Long userId,
        String licenseNumber,
        String assignedVehicleId
) {}
