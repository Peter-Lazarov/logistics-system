package com.sunny.times.shipments.domain.model;

public enum ShipmentStatus {
    CREATED,
    READY_FOR_PICKUP,
    IN_TRANSIT,
    ARRIVED_AT_WAREHOUSE,
    OUT_FOR_DELIVERY,
    DELIVERED,
    FAILED_DELIVERY,
    CANCELLED
}

