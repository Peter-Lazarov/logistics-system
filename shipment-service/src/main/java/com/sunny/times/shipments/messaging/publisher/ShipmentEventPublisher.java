package com.sunny.times.shipments.messaging.publisher;

import com.sunny.times.shipments.messaging.event.ShipmentCreatedEvent;
import com.sunny.times.shipments.messaging.event.ShipmentInTransitEvent;
import com.sunny.times.shipments.messaging.event.ShipmentArrivedEvent;
import com.sunny.times.shipments.messaging.event.ShipmentDeliveredEvent;
import com.sunny.times.shipments.messaging.event.ShipmentFailedEvent;
import com.sunny.times.shipments.messaging.event.ShipmentOutForDeliveryEvent;

public interface ShipmentEventPublisher {

    void publishCreated(ShipmentCreatedEvent event);

    void publishInTransit(ShipmentInTransitEvent event);

    void publishArrived(ShipmentArrivedEvent event);

    void publishDelivered(ShipmentDeliveredEvent event);

    void publishOutForDelivery(ShipmentOutForDeliveryEvent event);

    void publishFailed(ShipmentFailedEvent event);
}
