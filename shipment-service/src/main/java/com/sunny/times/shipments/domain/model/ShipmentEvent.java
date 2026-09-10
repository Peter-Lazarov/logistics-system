package com.sunny.times.shipments.domain.model;

public enum ShipmentEvent {
    ASSIGN_DRIVER,
    LOAD_SHIPMENT,
    START_ROUTE,
    ARRIVE_DESTINATION,
    COMPLETE_DELIVERY,
    DELAY,
    CANCEL
}
