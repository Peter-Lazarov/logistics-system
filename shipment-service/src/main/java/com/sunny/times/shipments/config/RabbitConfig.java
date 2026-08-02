package com.sunny.times.shipments.config;

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
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
