package com.sunny.times.tracking_api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String TRACKING_EXCHANGE =
            "tracking.exchange";

    public static final String TRACKING_QUEUE =
            "tracking.queue";

    public static final String ROUTING_KEY =
            "tracking.location";

    @Bean
    public TopicExchange trackingExchange() {
        return new TopicExchange(TRACKING_EXCHANGE);
    }

    @Bean
    public Queue trackingQueue() {
        return new Queue(TRACKING_QUEUE);
    }

    @Bean
    public Binding trackingBinding(
            Queue trackingQueue,
            TopicExchange trackingExchange
    ) {
        return BindingBuilder
                .bind(trackingQueue)
                .to(trackingExchange)
                .with(ROUTING_KEY);
    }

}