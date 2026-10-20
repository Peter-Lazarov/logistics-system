package com.sunny.times.tracking.domain.model;

import java.time.Instant;

public class VehicleLocation {

    private String vehicleId;
    private Double latitude;
    private Double longitude;
    private Instant timestamp;

    public VehicleLocation(
            String vehicleId,
            Double latitude,
            Double longitude,
            Instant timestamp
    ) {
        this.vehicleId = vehicleId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}