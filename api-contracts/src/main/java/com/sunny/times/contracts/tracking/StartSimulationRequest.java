package com.sunny.times.contracts.tracking;

public record StartSimulationRequest(
        String shipmentId,
        String vehicleId,
        String route
) {}