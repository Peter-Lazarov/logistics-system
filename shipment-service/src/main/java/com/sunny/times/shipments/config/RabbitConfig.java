package com.sunny.times.shipments.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange shipmentsExchange() {
        return new TopicExchange("shipments.exchange");
    }

    @Bean
    public Queue createdQueue() {
        return new Queue("shipment.created", true);
    }

    @Bean
    public Binding createdBinding(Queue createdQueue, TopicExchange shipmentsExchange) {
        return BindingBuilder.bind(createdQueue)
                .to(shipmentsExchange)
                .with("shipment.created");
    }

    @Bean
    public Queue inTransitQueue() {
        return new Queue("shipment.in_transit", true);
    }

    @Bean
    public Binding inTransitBinding(Queue inTransitQueue, TopicExchange shipmentsExchange) {
        return BindingBuilder.bind(inTransitQueue)
                .to(shipmentsExchange)
                .with("shipment.in_transit");
    }

    @Bean
    public Queue arrivedQueue() {
        return new Queue("shipment.arrived", true);
    }

    @Bean
    public Binding arrivedBinding(Queue arrivedQueue, TopicExchange shipmentsExchange) {
        return BindingBuilder.bind(arrivedQueue)
                .to(shipmentsExchange)
                .with("shipment.arrived");
    }

    @Bean
    public Queue outForDeliveryQueue() {
        return new Queue("shipment.out_for_delivery", true);
    }

    @Bean
    public Binding outForDeliveryBinding(Queue outForDeliveryQueue, TopicExchange shipmentsExchange) {
        return BindingBuilder.bind(outForDeliveryQueue)
                .to(shipmentsExchange)
                .with("shipment.out_for_delivery");
    }

    @Bean
    public Queue deliveredQueue() {
        return new Queue("shipment.delivered", true);
    }

    @Bean
    public Binding deliveredBinding(Queue deliveredQueue, TopicExchange shipmentsExchange) {
        return BindingBuilder.bind(deliveredQueue)
                .to(shipmentsExchange)
                .with("shipment.delivered");
    }

    @Bean
    public Queue failedQueue() {
        return new Queue("shipment.failed", true);
    }

    @Bean
    public Binding failedBinding(Queue failedQueue, TopicExchange shipmentsExchange) {
        return BindingBuilder.bind(failedQueue)
                .to(shipmentsExchange)
                .with("shipment.failed");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
