package com.sunny.times.contracts.drivers;

public record DriverDto(
        String id,
        String name,
        String phone,
        String licenseNumber,
        String assignedVehicleId
) {}
