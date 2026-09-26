package com.sunny.times.tracking.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "vehicle_locations")
public class VehicleLocationEntity {

    @Id
    private String id;

    private String vehicleId;

    private Double lat;

    private Double lng;

    private Instant timestamp;

    public VehicleLocationEntity() {
    }

    public VehicleLocationEntity(
            String id,
            String vehicleId,
            Double lat,
            Double lng,
            Instant timestamp
    ) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.lat = lat;
        this.lng = lng;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
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