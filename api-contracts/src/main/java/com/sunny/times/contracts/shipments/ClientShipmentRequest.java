package com.sunny.times.contracts.shipments;

public record ClientShipmentRequest(
        String category,
        String description,
        String origin,
        String destination,
        double totalWeight,
        double totalVolume
) {}

