package com.sunny.times.tracking.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "shipments.exchange";
    public static final String QUEUE = "shipments.created.queue";
    public static final String ROUTING_KEY = "shipment.created";
    public static final String ROUTING_KEY_IN_TRANSIT = "shipment.in_transit";
    public static final String ROUTING_KEY_ARRIVED = "shipment.arrived";
    public static final String ROUTING_KEY_OUT_FOR_DELIVERY = "shipment.out_for_delivery";
    public static final String ROUTING_KEY_DELIVERED = "shipment.delivered";
    public static final String ROUTING_KEY_FAILED = "shipment.failed";

    @Bean
    public TopicExchange shipmentsExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue shipmentsCreatedQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding shipmentsCreatedBinding(Queue shipmentsCreatedQueue, TopicExchange shipmentsExchange) {
        return BindingBuilder.bind(shipmentsCreatedQueue)
                .to(shipmentsExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public Queue shipmentsInTransitQueue() {
        return new Queue("shipments.in_transit.queue", true);
    }

    @Bean
    public Binding shipmentsInTransitBinding(Queue shipmentsInTransitQueue, TopicExchange shipmentsExchange) {
        return BindingBuilder.bind(shipmentsInTransitQueue)
                .to(shipmentsExchange)
                .with(ROUTING_KEY_IN_TRANSIT);
    }


    @Bean
    public Queue shipmentsArrivedQueue() {
        return new Queue("shipments.arrived.queue", true);
    }

    @Bean
    public Binding shipmentsArrivedBinding(Queue shipmentsArrivedQueue, TopicExchange shipmentsExchange) {
        return BindingBuilder.bind(shipmentsArrivedQueue)
                .to(shipmentsExchange)
                .with(ROUTING_KEY_ARRIVED);
    }


    @Bean
    public Queue shipmentsOutForDeliveryQueue() {
        return new Queue("shipments.out_for_delivery.queue", true);
    }

    @Bean
    public Binding shipmentsOutForDeliveryBinding(Queue shipmentsOutForDeliveryQueue, TopicExchange shipmentsExchange) {
        return BindingBuilder.bind(shipmentsOutForDeliveryQueue)
                .to(shipmentsExchange)
                .with(ROUTING_KEY_OUT_FOR_DELIVERY);
    }

    @Bean
    public Queue shipmentsDeliveredQueue() {
        return new Queue("shipments.delivered.queue", true);
    }

    @Bean
    public Binding shipmentsDeliveredBinding(Queue shipmentsDeliveredQueue, TopicExchange shipmentsExchange) {
        return BindingBuilder.bind(shipmentsDeliveredQueue)
                .to(shipmentsExchange)
                .with(ROUTING_KEY_DELIVERED);
    }

    @Bean
    public Queue shipmentsFailedQueue() {
        return new Queue("shipments.failed.queue", true);
    }

    @Bean
    public Binding shipmentsFailedBinding(Queue shipmentsFailedQueue, TopicExchange shipmentsExchange) {
        return BindingBuilder.bind(shipmentsFailedQueue)
                .to(shipmentsExchange)
                .with(ROUTING_KEY_FAILED);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
