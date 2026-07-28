package com.sunny.times.tracking.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Document("tracking_events")
public class TrackingEvent {

    @Id
    private String id;

    private UUID shipmentId;
    private String eventType;
    private Instant timestamp;

    public TrackingEvent(UUID shipmentId, String eventType, Instant timestamp) {
        this.shipmentId = shipmentId;
        this.eventType = eventType;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public UUID getShipmentId() {
        return shipmentId;
    }

    public String getEventType() {
        return eventType;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
