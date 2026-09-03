package com.sunny.times.shipments.messaging.publisher;

import com.sunny.times.shipments.messaging.event.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class RabbitShipmentEventPublisher implements ShipmentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "shipments.exchange";

    public RabbitShipmentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishShipmentCreated(ShipmentCreatedEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, "shipment.created", event);
    }

    @Override
    public void publishShipmentInTransit(ShipmentInTransitEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, "shipment.in_transit", event);
    }

    @Override
    public void publishShipmentArrived(ShipmentArrivedEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, "shipment.arrived", event);
    }

    @Override
    public void publishShipmentOutForDelivery(ShipmentOutForDeliveryEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, "shipment.out_for_delivery", event);
    }

    @Override
    public void publishShipmentDelivered(ShipmentDeliveredEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, "shipment.delivered", event);
    }

    @Override
    public void publishShipmentFailed(ShipmentFailedEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, "shipment.failed", event);
    }

}
