package com.sunny.times.contracts.tracking;

public record VehicleLocationDto(
        String vehicleId,
        double latitude,
        double longitude,
        String timestamp
) {}
