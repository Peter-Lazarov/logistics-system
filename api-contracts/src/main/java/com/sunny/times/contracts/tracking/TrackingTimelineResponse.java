package com.sunny.times.contracts.tracking;

import java.util.List;
import java.util.UUID;

public class TrackingTimelineResponse {

    private UUID shipmentId;
    private List<TrackingEventResponse> events;

    public TrackingTimelineResponse(UUID shipmentId, List<TrackingEventResponse> events) {
        this.shipmentId = shipmentId;
        this.events = events;
    }

    public UUID getShipmentId() {
        return shipmentId;
    }

    public List<TrackingEventResponse> getEvents() {
        return events;
    }
}
