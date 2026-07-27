package com.sunny.times.shipments.messaging.event;

import java.time.Instant;
import java.util.UUID;

public class ShipmentCreatedEvent {

    private UUID shipmentId;
    private UUID orderId;
    private Instant createdAt;

    public ShipmentCreatedEvent(UUID shipmentId, UUID orderId, Instant createdAt) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.createdAt = createdAt;
    }

    public UUID getShipmentId() {
        return shipmentId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
