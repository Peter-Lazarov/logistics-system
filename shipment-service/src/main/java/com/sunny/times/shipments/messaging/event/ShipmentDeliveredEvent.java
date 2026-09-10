package com.sunny.times.shipments.messaging.event;

public class ShipmentDeliveredEvent {

    private String shipmentId;

    public ShipmentDeliveredEvent() {
    }

    public ShipmentDeliveredEvent(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }
}
