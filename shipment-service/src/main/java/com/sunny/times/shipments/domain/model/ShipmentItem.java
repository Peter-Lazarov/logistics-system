package com.sunny.times.movement.shipments.domain.model;

import java.util.UUID;

public class ShipmentItem {

    private final UUID id;
    private final String sku;
    private final int quantity;
    private final double weight;

    public ShipmentItem(
            UUID id,
            String sku,
            int quantity,
            double weight
    ) {
        this.id = id;
        this.sku = sku;
        this.quantity = quantity;
        this.weight = weight;
    }

    public UUID getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getWeight() {
        return weight;
    }
}

