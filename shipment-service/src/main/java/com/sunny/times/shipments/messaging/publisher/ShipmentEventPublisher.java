package com.sunny.times.shipments.messaging.publisher;

import com.sunny.times.shipments.messaging.event.ShipmentCreatedEvent;

public interface ShipmentEventPublisher {
    void publishShipmentCreated(ShipmentCreatedEvent event);
}
