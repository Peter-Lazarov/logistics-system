package com.sunny.times.contracts.shipments;

public record ClientShipmentRequest(
        String category,
        String description,
        String origin,
        String destination,
        String simulationRoute,
        double totalWeight,
        double totalVolume
) {}

