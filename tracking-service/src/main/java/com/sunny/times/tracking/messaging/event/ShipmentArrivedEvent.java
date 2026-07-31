package com.sunny.times.tracking.messaging.event;

import java.time.Instant;
import java.util.UUID;

public class ShipmentArrivedEvent {

    private UUID shipmentId;
    private UUID orderId;
    private Instant timestamp;

    public ShipmentArrivedEvent() {
    }

    public UUID getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(UUID shipmentId) {
        this.shipmentId = shipmentId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
