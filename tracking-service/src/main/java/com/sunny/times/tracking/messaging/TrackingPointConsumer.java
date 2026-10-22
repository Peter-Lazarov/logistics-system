package com.sunny.times.tracking.messaging;

import com.sunny.times.contracts.tracking.TrackingPointEvent;
import com.sunny.times.tracking.domain.service.VehicleLocationService;
import com.sunny.times.tracking.persistence.entity.VehicleLocationEntity;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TrackingPointConsumer {

    private final VehicleLocationService service;

    public TrackingPointConsumer(
            VehicleLocationService service
    ) {
        this.service = service;
    }

    @RabbitListener(queues = "tracking.queue")
    public void consume(
            TrackingPointEvent event
    ) {
                if (event.shipmentId() == null || event.shipmentId().isBlank()) {
                        throw new IllegalArgumentException("Tracking points must belong to a shipment.");
                }

        VehicleLocationEntity entity =
                new VehicleLocationEntity(
                        null,
                        event.shipmentId(),
                        event.vehicleId(),
                        event.latitude(),
                        event.longitude(),
                        Instant.parse(event.timestamp())
                );

        service.save(entity);

        System.out.println(
                "GPS point saved for shipment: " + event.shipmentId()
        );
    }
}