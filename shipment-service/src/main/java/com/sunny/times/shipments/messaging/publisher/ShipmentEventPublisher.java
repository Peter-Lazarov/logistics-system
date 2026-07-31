package com.sunny.times.shipments.messaging.publisher;

import com.sunny.times.shipments.messaging.event.*;

public interface ShipmentEventPublisher {
    void publishShipmentCreated(ShipmentCreatedEvent event);

    void publishShipmentInTransit(ShipmentInTransitEvent event);

    void publishShipmentArrived(ShipmentArrivedEvent event);

    void publishShipmentOutForDelivery(ShipmentOutForDeliveryEvent event);

    void publishShipmentDelivered(ShipmentDeliveredEvent event);

    void publishShipmentFailed(ShipmentFailedEvent event);

}
