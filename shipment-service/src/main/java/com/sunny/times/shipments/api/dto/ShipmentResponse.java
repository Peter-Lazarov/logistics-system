package com.sunny.times.shipments.api.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ShipmentResponse {

    private final UUID id;
    private final UUID orderId;
    private final ShipmentStatusDto status;
    private final UUID originWarehouseId;
    private final UUID destinationWarehouseId;
    private final UUID routeId;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final List<ShipmentItemDto> items;

    public ShipmentResponse(
            UUID id,
            UUID orderId,
            ShipmentStatusDto status,
            UUID originWarehouseId,
            UUID destinationWarehouseId,
            UUID routeId,
            Instant createdAt,
            Instant updatedAt,
            List<ShipmentItemDto> items
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

    public ShipmentStatusDto getStatus() {
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

    public List<ShipmentItemDto> getItems() {
        return items;
    }
}
