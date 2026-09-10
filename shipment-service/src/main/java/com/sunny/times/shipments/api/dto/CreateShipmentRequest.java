package com.sunny.times.shipments.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class CreateShipmentRequest {

    @NotBlank
    private String type;

    @NotNull
    @Positive
    private Integer quantity;

    @NotNull
    @Positive
    private Double weight;

    @NotNull
    @Positive
    private Double volume;

    @NotBlank
    private String origin;

    @NotBlank
    private String destination;

    @NotBlank
    private String vehicleId;

    @NotBlank
    private String driverId;

    @NotBlank
    private String pathId;

    @NotNull
    private String price;

    @NotNull
    @Valid
    private List<ShipmentItemDto> items;

    public CreateShipmentRequest() {
    }

    public String getType() {
        return type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getVolume() {
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

    public String getPrice() {
        return price;
    }

    public List<ShipmentItemDto> getItems() {
        return items;
    }
}
