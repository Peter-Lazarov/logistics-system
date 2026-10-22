package com.sunny.times.tracking_api.messaging;

import com.sunny.times.contracts.tracking.TrackingPointEvent;
import com.sunny.times.tracking_api.config.RabbitConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TrackingProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TrackingProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(TrackingPointEvent event) {

        byte[] json;
        try {
            json = objectMapper.writeValueAsBytes(event);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Could not serialize tracking point", exception);
        }
        Message message = MessageBuilder
            .withBody(json)
                .setContentType("application/json")
                .build();

        rabbitTemplate.send(
                RabbitConfig.TRACKING_EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                message
        );
    }
}