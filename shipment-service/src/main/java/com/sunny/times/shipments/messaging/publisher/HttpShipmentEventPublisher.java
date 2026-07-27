package com.sunny.times.shipments.messaging.publisher;

import com.sunny.times.shipments.messaging.event.ShipmentCreatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class HttpShipmentEventPublisher implements ShipmentEventPublisher{
    private final WebClient webClient;

    public HttpShipmentEventPublisher(WebClient.Builder builder){
        this.webClient = builder.baseUrl("http://localhost:8082").build();
    }

    @Override
    public void publishShipmentCreated(ShipmentCreatedEvent event) {
        webClient.post()
                .uri("/tracking/events")
                .bodyValue(event)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
