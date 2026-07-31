package com.sunny.times.tracking.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "tracking_events")
public class TrackingEventEntity {

    @Id
    private String id;

    private UUID shipmentId;
    private UUID orderId;
    private String eventType;
    private Instant timestamp;
    private String reason;   // <-- добавено поле

    public TrackingEventEntity(String id, UUID shipmentId, UUID orderId, String eventType, Instant timestamp, String reason) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.reason = reason;
    }

    public String getId() {
        return id;
    }

    public UUID getShipmentId() {
        return shipmentId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getEventType() {
        return eventType;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getReason() {
        return reason;
    }
}
