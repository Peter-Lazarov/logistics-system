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
        String pathId,
        double totalWeight,
        double totalVolume,
        double price,
        String status,
        String createdAt,
        String updatedAt
) {}