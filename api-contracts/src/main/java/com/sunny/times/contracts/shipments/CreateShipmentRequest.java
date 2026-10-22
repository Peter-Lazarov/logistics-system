package com.sunny.times.contracts.shipments;

public record CreateShipmentRequest(
        String category,
        String description,
        String origin,
        String destination,
        Long clientUserId,
        Long driverUserId,
        String vehicleId,
        String simulationRoute,
        double totalWeight,
        double totalVolume,
        double price
) {}
