package com.sunny.times.movement.shipments.api.dto;

import com.sunny.times.movement.shipments.domain.model.ShipmentItem;
import com.sunny.times.movement.shipments.domain.model.ShipmentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class CreateShipmentRequest {

    @NotNull
    private UUID orderId;

    @NotNull
    private ShipmentStatus status;

    @NotNull
    private UUID originWarehouseId;

    @NotNull
    private UUID destinationWarehouseId;

    @NotNull
    private UUID routeId;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    @Size(min = 1)
    private List<ShipmentItem> items;

    public CreateShipmentRequest() {
        // празен конструктор за JSON десериализация
    }

    public CreateShipmentRequest(
            UUID orderId,
            ShipmentStatus status,
            UUID originWarehouseId,
            UUID destinationWarehouseId,
            UUID routeId,
            Instant createdAt,
            Instant updatedAt,
            List<ShipmentItem> items
    ) {
        this.orderId = orderId;
        this.status = status;
        this.originWarehouseId = originWarehouseId;
        this.destinationWarehouseId = destinationWarehouseId;
        this.routeId = routeId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.items = items;
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
