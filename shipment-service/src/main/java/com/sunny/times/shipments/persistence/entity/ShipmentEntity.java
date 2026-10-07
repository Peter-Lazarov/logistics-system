package com.sunny.times.shipments.persistence.entity;

import com.sunny.times.shipments.domain.model.ShipmentStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "shipments")
public class ShipmentEntity {

    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private Long clientUserId;

    @Column(nullable = true)
    private Long driverUserId;

    @Column(nullable = true)
    private String vehicleId;

    @Column(nullable = true)
    private String pathId;

    @Column(nullable = false)
    private Double totalWeight;

    @Column(nullable = false)
    private Double totalVolume;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipmentStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    @Column
    private Instant updatedAt;

    public ShipmentEntity() {}

    public ShipmentEntity(String id,
                          String category,
                          String description,
                          String origin,
                          String destination,
                          Long clientUserId,
                          Long driverUserId,
                          String vehicleId,
                          String pathId,
                          Double totalWeight,
                          Double totalVolume,
                          Double price,
                          ShipmentStatus status,
                          Instant createdAt,
                          Instant updatedAt) {

        this.id = id;
        this.category = category;
        this.description = description;
        this.origin = origin;
        this.destination = destination;
        this.clientUserId = clientUserId;
        this.driverUserId = driverUserId;
        this.vehicleId = vehicleId;
        this.pathId = pathId;
        this.totalWeight = totalWeight;
        this.totalVolume = totalVolume;
        this.price = price;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Long getClientUserId() { return clientUserId; }
    public void setClientUserId(Long clientUserId) { this.clientUserId = clientUserId; }

    public Long getDriverUserId() { return driverUserId; }
    public void setDriverUserId(Long driverUserId) { this.driverUserId = driverUserId; }

    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }

    public String getPathId() { return pathId; }
    public void setPathId(String pathId) { this.pathId = pathId; }

    public Double getTotalWeight() { return totalWeight; }
    public void setTotalWeight(Double totalWeight) { this.totalWeight = totalWeight; }

    public Double getTotalVolume() { return totalVolume; }
    public void setTotalVolume(Double totalVolume) { this.totalVolume = totalVolume; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public ShipmentStatus getStatus() { return status; }
    public void setStatus(ShipmentStatus status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
