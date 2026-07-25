package com.sunny.times.movement.shipments.persistence.entity;

import com.sunny.times.movement.shipments.domain.model.ShipmentStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shipments")
public class ShipmentEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID orderId;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    private UUID originWarehouseId;

    private UUID destinationWarehouseId;

    private UUID routeId;

    private Instant createdAt;

    private Instant updatedAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "shipment_id")
    private List<ShipmentItemEntity> items;

    public ShipmentEntity() {
        // JPA requires empty constructor
    }

    public ShipmentEntity(
            UUID id,
            UUID orderId,
            ShipmentStatus status,
            UUID originWarehouseId,
            UUID destinationWarehouseId,
            UUID routeId,
            Instant createdAt,
            Instant updatedAt,
            List<ShipmentItemEntity> items
    ) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.originWarehouseId = originWarehouseId;
        this.destinationWarehouseId = destinationWarehouseId;
        this.routeId = routeId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.items = items;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public UUID getOriginWarehouseId() {
        return originWarehouseId;
    }

    public UUID getDestinationWarehouseId() {
        return destinationWarehouseId;
    }

    public UUID getRouteId() {
        return routeId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public List<ShipmentItemEntity> getItems() {
        return items;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public void setOriginWarehouseId(UUID originWarehouseId) {
        this.originWarehouseId = originWarehouseId;
    }

    public void setDestinationWarehouseId(UUID destinationWarehouseId) {
        this.destinationWarehouseId = destinationWarehouseId;
    }

    public void setRouteId(UUID routeId) {
        this.routeId = routeId;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setItems(List<ShipmentItemEntity> items) {
        this.items = items;
    }
}
