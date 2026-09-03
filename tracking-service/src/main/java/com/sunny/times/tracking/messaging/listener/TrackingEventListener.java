package com.sunny.times.tracking.messaging.listener;

import com.sunny.times.tracking.domain.service.TrackingEventService;
import com.sunny.times.tracking.messaging.event.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TrackingEventListener {

    private final TrackingEventService trackingEventService;

    public TrackingEventListener(TrackingEventService trackingEventService) {
        this.trackingEventService = trackingEventService;
    }

    @RabbitListener(queues = "shipment.created")
    public void handleCreated(ShipmentCreatedEvent event) {
        trackingEventService.addCreatedEvent(
                event.getShipmentId(),
                event.getOrderId(),
                event.getCreatedAt()
        );
    }

    @RabbitListener(queues = "shipment.in_transit")
    public void handleInTransit(ShipmentInTransitEvent event) {
        trackingEventService.addInTransitEvent(
                event.getShipmentId(),
                event.getOrderId(),
                event.getTimestamp()
        );
    }

    @RabbitListener(queues = "shipment.out_for_delivery")
    public void handleOutForDelivery(ShipmentOutForDeliveryEvent event) {
        trackingEventService.addOutForDeliveryEvent(
                event.getShipmentId(),
                event.getOrderId(),
                event.getTimestamp()
        );
    }

    @RabbitListener(queues = "shipment.delivered")
    public void handleDelivered(ShipmentDeliveredEvent event) {
        trackingEventService.addDeliveredEvent(
                event.getShipmentId(),
                event.getOrderId(),
                event.getTimestamp()
        );
    }

    @RabbitListener(queues = "shipment.failed")
    public void handleFailed(ShipmentFailedEvent event) {
        trackingEventService.addFailedEvent(
                event.getShipmentId(),
                event.getOrderId(),
                event.getTimestamp(),
                event.getReason()
        );
    }
}
