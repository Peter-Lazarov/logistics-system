package com.sunny.times.shipments.messaging.event;

public class ShipmentCreatedEvent {

    private String shipmentId;

    public ShipmentCreatedEvent() {
    }

    public ShipmentCreatedEvent(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }
}
