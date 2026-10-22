package com.sunny.times.contracts.tracking;

public record VehicleLocationDto(
        String shipmentId,
        String vehicleId,
        double latitude,
        double longitude,
        String timestamp
) {}
