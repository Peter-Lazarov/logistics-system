package com.sunny.times.movement.shipments.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Shipment {

    private final UUID id;
    private final UUID orderId;
    private final ShipmentStatus status;
    private final UUID originWarehouseId;
    private final UUID destinationWarehouseId;
    private final UUID routeId;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final List<ShipmentItem> items;

    public Shipment(
            UUID id,
            UUID orderId,
            ShipmentStatus status,
            UUID originWarehouseId,
            UUID destinationWarehouseId,
            UUID routeId,
            Instant createdAt,
            Instant updatedAt,
            List<ShipmentItem> items
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

    public List<ShipmentItem> getItems() {
        return items;
    }
}
