package com.sunny.times.shipments.api.dto;

import com.sunny.times.shipments.domain.model.Shipment;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class ShipmentResponse {

    private final String id;
    private final String type;
    private final int quantity;
    private final double weight;
    private final double volume;
    private final String origin;
    private final String destination;
    private final String vehicleId;
    private final String driverId;
    private final String pathId;
    private final ShipmentStatusDto status;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final List<ShipmentItemDto> items;

    public ShipmentResponse(
            String id,
            String type,
            int quantity,
            double weight,
            double volume,
            String origin,
            String destination,
            String vehicleId,
            String driverId,
            String pathId,
            ShipmentStatusDto status,
            Instant createdAt,
            Instant updatedAt,
            List<ShipmentItemDto> items
    ) {
        this.id = id;
        this.type = type;
        this.quantity = quantity;
        this.weight = weight;
        this.volume = volume;
        this.origin = origin;
        this.destination = destination;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.pathId = pathId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.items = items;
    }

    public static ShipmentResponse fromDomain(Shipment domain) {
        return new ShipmentResponse(
                domain.getId(),
                domain.getType(),
                domain.getQuantity(),
                domain.getWeight(),
                domain.getVolume(),
                domain.getOrigin(),
                domain.getDestination(),
                domain.getVehicleId(),
                domain.getDriverId(),
                domain.getPathId(),
                ShipmentStatusDto.valueOf(domain.getStatus().name()),
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                domain.getItems().stream()
                        .map(i -> new ShipmentItemDto(
                                i.getId(),
                                i.getName(),
                                i.getDescription(),
                                i.getQuantity()
                        ))
                        .toList()

        );
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getWeight() {
        return weight;
    }

    public double getVolume() {
        return volume;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getPathId() {
        return pathId;
    }

    public ShipmentStatusDto getStatus() {
        return status;
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
