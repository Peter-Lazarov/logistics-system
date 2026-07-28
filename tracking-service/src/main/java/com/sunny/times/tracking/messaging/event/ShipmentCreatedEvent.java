package com.sunny.times.tracking.messaging.event;

import java.util.UUID;

public class ShipmentCreatedEvent {
    private UUID shipmentId;
    private UUID orderId;

    public ShipmentCreatedEvent(){

    }

    public ShipmentCreatedEvent(UUID shipmentId, UUID orderId){
        this.shipmentId = shipmentId;
        this.orderId = orderId;
    }

    public UUID getShipmentId() {
        return shipmentId;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
