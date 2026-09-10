package com.sunny.times.shipments.messaging.publisher;

import com.sunny.times.shipments.messaging.event.ShipmentCreatedEvent;
import com.sunny.times.shipments.messaging.event.ShipmentInTransitEvent;
import com.sunny.times.shipments.messaging.event.ShipmentArrivedEvent;
import com.sunny.times.shipments.messaging.event.ShipmentDeliveredEvent;
import com.sunny.times.shipments.messaging.event.ShipmentFailedEvent;
import com.sunny.times.shipments.messaging.event.ShipmentOutForDeliveryEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitShipmentEventPublisher implements ShipmentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitShipmentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishCreated(ShipmentCreatedEvent event) {
        rabbitTemplate.convertAndSend("shipment.exchange", "shipment.created", event);
    }

    @Override
    public void publishInTransit(ShipmentInTransitEvent event) {
        rabbitTemplate.convertAndSend("shipment.exchange", "shipment.in_transit", event);
    }

    @Override
    public void publishArrived(ShipmentArrivedEvent event) {
        rabbitTemplate.convertAndSend("shipment.exchange", "shipment.arrived", event);
    }

    @Override
    public void publishDelivered(ShipmentDeliveredEvent event) {
        rabbitTemplate.convertAndSend("shipment.exchange", "shipment.delivered", event);
    }

    @Override
    public void publishOutForDelivery(ShipmentOutForDeliveryEvent event) {
        rabbitTemplate.convertAndSend("shipment.exchange", "shipment.out_for_delivery", event);
    }

    @Override
    public void publishFailed(ShipmentFailedEvent event) {
        rabbitTemplate.convertAndSend("shipment.exchange", "shipment.failed", event);
    }
}
