package com.sunny.times.tracking.api.dto;

import java.time.Instant;
import java.util.UUID;

public class TrackingEventResponse {

    private final UUID shipmentId;
    private final String status;
    private final Instant timestamp;
    private final String reason;

    public TrackingEventResponse(UUID shipmentId, String status, Instant timestamp, String reason) {
        this.shipmentId = shipmentId;
        this.status = status;
        this.timestamp = timestamp;
        this.reason = reason;
    }

    public UUID getShipmentId() {
        return shipmentId;
    }

    public String getStatus() {
        return status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getReason() {
        return reason;
    }
}
