package com.sunny.times.tracking_api.messaging;

import com.sunny.times.contracts.tracking.TrackingPointEvent;
import com.sunny.times.tracking_api.config.RabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TrackingProducer {

    private final RabbitTemplate rabbitTemplate;

    public TrackingProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(TrackingPointEvent event) {

        String json = """
                {
                  "vehicleId":"%s",
                  "lat":%s,
                  "lng":%s,
                  "timestamp":"%s"
                }
                """.formatted(
                event.vehicleId(),
                event.lat(),
                event.lng(),
                event.timestamp()
        );
        Message message = MessageBuilder
                .withBody(json.getBytes())
                .setContentType("application/json")
                .build();

        rabbitTemplate.send(
                RabbitConfig.TRACKING_EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                message
        );
    }
}