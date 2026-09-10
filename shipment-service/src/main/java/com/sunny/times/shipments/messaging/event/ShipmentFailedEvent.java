package com.sunny.times.shipments.messaging.event;

public class ShipmentFailedEvent {

    private String shipmentId;

    public ShipmentFailedEvent() {
    }

    public ShipmentFailedEvent(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }
}
