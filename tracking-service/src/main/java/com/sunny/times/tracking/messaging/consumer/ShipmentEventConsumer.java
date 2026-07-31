package com.sunny.times.tracking.messaging.consumer;

import com.sunny.times.tracking.domain.service.TrackingEventService;
import com.sunny.times.tracking.messaging.event.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ShipmentEventConsumer {

    private final TrackingEventService trackingEventService;

    public ShipmentEventConsumer(TrackingEventService trackingEventService) {
        this.trackingEventService = trackingEventService;
    }

    @RabbitListener(queues = "shipments.created.queue")
    public void handleShipmentCreated(ShipmentCreatedEvent event) {
        trackingEventService.addCreatedEvent(
                event.getShipmentId(),
                event.getOrderId(),
                event.getCreatedAt()
        );
    }

    @RabbitListener(queues = "shipments.in_transit.queue")
    public void handleShipmentInTransit(ShipmentInTransitEvent event) {
        trackingEventService.addInTransitEvent(
                event.getShipmentId(),
                event.getOrderId(),
                event.getTimestamp()
        );
    }

    @RabbitListener(queues = "shipments.arrived.queue")
    public void handleShipmentArrived(ShipmentArrivedEvent event) {
        trackingEventService.addArrivedEvent(
                event.getShipmentId(),
                event.getOrderId(),
                event.getTimestamp()
        );
    }

    @RabbitListener(queues = "shipments.out_for_delivery.queue")
    public void handleShipmentOutForDelivery(ShipmentOutForDeliveryEvent event) {
        trackingEventService.addOutForDeliveryEvent(
                event.getShipmentId(),
                event.getOrderId(),
                event.getTimestamp()
        );
    }

    @RabbitListener(queues = "shipments.delivered.queue")
    public void handleShipmentDelivered(ShipmentDeliveredEvent event) {
        trackingEventService.addDeliveredEvent(
                event.getShipmentId(),
                event.getOrderId(),
                event.getTimestamp()
        );
    }

    @RabbitListener(queues = "shipments.failed.queue")
    public void handleShipmentFailed(ShipmentFailedEvent event) {
        trackingEventService.addFailedEvent(
                event.getShipmentId(),
                event.getOrderId(),
                event.getTimestamp(),
                event.getReason()
        );
    }

}
