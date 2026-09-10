package com.sunny.times.shipments.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange shipmentExchange() {
        return new TopicExchange("shipment.exchange");
    }
}
