package com.sunny.times.tracking.messaging.event;

import java.time.Instant;
import java.util.UUID;

public class ShipmentDeliveredEvent {

    private UUID shipmentId;
    private UUID orderId;
    private Instant timestamp;

    public ShipmentDeliveredEvent() {
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
