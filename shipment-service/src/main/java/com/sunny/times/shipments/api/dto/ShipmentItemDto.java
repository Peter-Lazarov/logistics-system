package com.sunny.times.shipments.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ShipmentItemDto {

    @NotBlank(message = "SKU cannot be blank")
    private String sku;

    @NotNull
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @NotNull
    @Positive(message = "Weight must be positive")
    private Double weight;

    public ShipmentItemDto() {
    }

    public ShipmentItemDto(String sku, Integer quantity, Double weight) {
        this.sku = sku;
        this.quantity = quantity;
        this.weight = weight;
    }

    public String getSku() {
        return sku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getWeight() {
        return weight;
    }
}
