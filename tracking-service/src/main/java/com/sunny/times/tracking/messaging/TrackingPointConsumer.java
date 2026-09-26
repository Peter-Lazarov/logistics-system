package com.sunny.times.tracking.messaging;

import com.sunny.times.contracts.tracking.TrackingPointEvent;
import com.sunny.times.tracking.domain.service.VehicleLocationService;
import com.sunny.times.tracking.persistence.entity.VehicleLocationEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TrackingPointConsumer {

    private final VehicleLocationService service;

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    public TrackingPointConsumer(
            VehicleLocationService service
    ) {
        this.service = service;
    }

    @RabbitListener(queues = "tracking.queue")
    public void consume(
            TrackingPointEvent event
    ) {

        VehicleLocationEntity entity =
                new VehicleLocationEntity(
                        null,
                        event.vehicleId(),
                        event.lat(),
                        event.lng(),
                        Instant.parse(event.timestamp())
                );

        service.save(entity);

        System.out.println(
                "GPS point saved for vehicle: "
                        + event.vehicleId()
        );
    }
}