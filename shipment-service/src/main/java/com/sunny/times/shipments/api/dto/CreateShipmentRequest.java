package com.sunny.times.shipments.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class CreateShipmentRequest {

    @NotNull
    private UUID orderId;

    @NotNull
    private ShipmentStatusDto status;

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

    @NotNull
    @Valid
    @Size(min = 1)
    private List<ShipmentItemDto> items;

    public CreateShipmentRequest() {

    }

    public CreateShipmentRequest(
            UUID orderId,
            ShipmentStatusDto status,
            UUID originWarehouseId,
            UUID destinationWarehouseId,
            UUID routeId,
            Instant createdAt,
            Instant updatedAt,
            List<ShipmentItemDto> items
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
