package com.sunny.times.tracking.messaging.event;

import java.time.Instant;
import java.util.UUID;

public class ShipmentFailedEvent {

    private UUID shipmentId;
    private UUID orderId;
    private Instant timestamp;
    private String reason;

    public ShipmentFailedEvent() {
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

    public String getReason() {
        return reason;
    }
}
