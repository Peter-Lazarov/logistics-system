package com.sunny.times.shipments.domain.exception;

import java.util.UUID;

public class ShipmentNotFoundException extends RuntimeException {
    public ShipmentNotFoundException(String id) {
        super("Shipment not found: " + id);
    }
}
