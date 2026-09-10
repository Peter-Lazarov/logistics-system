package com.sunny.times.shipments.messaging.event;

public class ShipmentOutForDeliveryEvent {

    private String shipmentId;

    public ShipmentOutForDeliveryEvent() {
    }

    public ShipmentOutForDeliveryEvent(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }
}
