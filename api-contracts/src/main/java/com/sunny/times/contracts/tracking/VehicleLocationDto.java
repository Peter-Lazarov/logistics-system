package com.sunny.times.contracts.tracking;

public record VehicleLocationDto(
        String vehicleId,
        double lat,
        double lng,
        String timestamp
) {}
