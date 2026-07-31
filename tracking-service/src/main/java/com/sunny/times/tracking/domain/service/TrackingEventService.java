package com.sunny.times.tracking.domain.service;

import com.sunny.times.tracking.persistence.entity.TrackingEventEntity;
import com.sunny.times.tracking.persistence.repository.TrackingEventRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class TrackingEventService {

    private final TrackingEventRepository trackingEventRepository;

    public TrackingEventService(TrackingEventRepository trackingEventRepository) {
        this.trackingEventRepository = trackingEventRepository;
    }

    public TrackingEventEntity addCreatedEvent(UUID shipmentId, UUID orderId, Instant createdAt) {
        TrackingEventEntity event = new TrackingEventEntity(
                null,
                shipmentId,
                orderId,
                "CREATED",
                createdAt,
                null
        );
        return trackingEventRepository.save(event);
    }

    public TrackingEventEntity addInTransitEvent(UUID shipmentId, UUID orderId, Instant timestamp) {
        TrackingEventEntity event = new TrackingEventEntity(
                null,
                shipmentId,
                orderId,
                "IN_TRANSIT",
                timestamp,
                null
        );
        return trackingEventRepository.save(event);
    }

    public TrackingEventEntity addArrivedEvent(UUID shipmentId, UUID orderId, Instant timestamp) {
        TrackingEventEntity event = new TrackingEventEntity(
                null,
                shipmentId,
                orderId,
                "ARRIVED_AT_WAREHOUSE",
                timestamp,
                null
        );
        return trackingEventRepository.save(event);
    }

    public TrackingEventEntity addOutForDeliveryEvent(UUID shipmentId, UUID orderId, Instant timestamp) {
        TrackingEventEntity event = new TrackingEventEntity(
                null,
                shipmentId,
                orderId,
                "OUT_FOR_DELIVERY",
                timestamp,
                null
        );
        return trackingEventRepository.save(event);
    }

    public TrackingEventEntity addDeliveredEvent(UUID shipmentId, UUID orderId, Instant timestamp) {
        TrackingEventEntity event = new TrackingEventEntity(
                null,
                shipmentId,
                orderId,
                "DELIVERED",
                timestamp,
                null
        );
        return trackingEventRepository.save(event);
    }

    public TrackingEventEntity addFailedEvent(UUID shipmentId, UUID orderId, Instant timestamp, String reason) {
        TrackingEventEntity event = new TrackingEventEntity(
                null,
                shipmentId,
                orderId,
                "FAILED_DELIVERY",
                timestamp,
                reason
        );
        return trackingEventRepository.save(event);
    }
}
