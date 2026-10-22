package com.sunny.times.tracking.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "vehicle_locations")
public class VehicleLocationEntity {

    @Id
    private String id;

    @Indexed
    private String shipmentId;

    private String vehicleId;

    private Double latitude;

    private Double longitude;

    private Instant timestamp;

    public VehicleLocationEntity() {
    }

    public VehicleLocationEntity(
            String id,
            String shipmentId,
            String vehicleId,
            Double latitude,
            Double longitude,
            Instant timestamp
    ) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.vehicleId = vehicleId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getShipmentId() {
        return shipmentId;
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