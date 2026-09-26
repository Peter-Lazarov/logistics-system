package com.sunny.times.tracking.domain.model;

import java.time.Instant;

public class VehicleLocation {

    private String vehicleId;
    private Double lat;
    private Double lng;
    private Instant timestamp;

    public VehicleLocation(
            String vehicleId,
            Double lat,
            Double lng,
            Instant timestamp
    ) {
        this.vehicleId = vehicleId;
        this.lat = lat;
        this.lng = lng;
        this.timestamp = timestamp;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}