package com.sunny.times.tracking.api.dto;

import java.time.Instant;

public class TrackingEventResponse {

    private String status;
    private Instant timestamp;
    private String reason;

    public TrackingEventResponse(String status, Instant timestamp, String reason) {
        this.status = status;
        this.timestamp = timestamp;
        this.reason = reason;
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
