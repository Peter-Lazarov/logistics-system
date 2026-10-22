package com.sunny.times.contracts.shipments;

public record ShipmentResponse(
        String id,
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
        Double distanceKm,
        double price,
        String status,
        String createdAt,
        String updatedAt
) {}