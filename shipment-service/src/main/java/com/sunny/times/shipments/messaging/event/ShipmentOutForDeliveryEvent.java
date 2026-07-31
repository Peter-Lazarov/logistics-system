package com.sunny.times.shipments.messaging.event;

import java.time.Instant;
import java.util.UUID;

public class ShipmentOutForDeliveryEvent {

    private UUID shipmentId;
    private UUID orderId;
    private Instant timestamp;

    public ShipmentOutForDeliveryEvent() {
    }

    public ShipmentOutForDeliveryEvent(UUID shipmentId, UUID orderId, Instant timestamp) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.timestamp = timestamp;
    }

    public UUID getShipmentId() {
        return shipmentId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
