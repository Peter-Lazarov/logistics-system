package com.sunny.times.shipments.messaging.event;

public class ShipmentArrivedEvent {

    private String shipmentId;

    public ShipmentArrivedEvent() {
    }

    public ShipmentArrivedEvent(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }
}
