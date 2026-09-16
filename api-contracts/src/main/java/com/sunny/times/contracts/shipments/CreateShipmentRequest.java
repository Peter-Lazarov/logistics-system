package com.sunny.times.contracts.shipments;

public record CreateShipmentRequest(
        String category,
        String description,
        String origin,
        String destination,
        String clientId,
        String driverId,
        String vehicleId,
        String pathId,
        double totalWeight,
        double totalVolume,
        double price
) {}
