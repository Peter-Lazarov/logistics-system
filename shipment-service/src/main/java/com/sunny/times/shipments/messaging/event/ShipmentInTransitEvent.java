package com.sunny.times.shipments.messaging.event;

public class ShipmentInTransitEvent {

    private String shipmentId;

    public ShipmentInTransitEvent() {
    }

    public ShipmentInTransitEvent(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }
}
