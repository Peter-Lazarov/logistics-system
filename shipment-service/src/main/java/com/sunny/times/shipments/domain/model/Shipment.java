package com.sunny.times.shipments.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class Shipment {

    private String id;
    private String type;
    private Integer quantity;
    private Double weight;
    private Double volume;
    private ShipmentStatus status;
    private String origin;
    private String destination;
    private String vehicleId;
    private String driverId;
    private String pathId;
    private BigDecimal price;
    private Instant createdAt;
    private Instant updatedAt;
    private List<ShipmentItem> items;

    public Shipment() {
    }

    public Shipment(String id,
                    String type,
                    Integer quantity,
                    Double weight,
                    Double volume,
                    ShipmentStatus status,
                    String origin,
                    String destination,
                    String vehicleId,
                    String driverId,
                    String pathId,
                    BigDecimal price,
                    Instant createdAt,
                    Instant updatedAt,
                    List<ShipmentItem> items) {
        this.id = id;
        this.type = type;
        this.quantity = quantity;
        this.weight = weight;
        this.volume = volume;
        this.status = status;
        this.origin = origin;
        this.destination = destination;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.pathId = pathId;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getPathId() {
        return pathId;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ShipmentItem> getItems() {
        return items;
    }

    public void setItems(List<ShipmentItem> items) {
        this.items = items;
    }
}
