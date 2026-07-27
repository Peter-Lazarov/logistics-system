package com.sunny.times.shipments.persistence.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "shipment_items")
public class ShipmentItemEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double weight;

    public ShipmentItemEntity() {
        // JPA requires empty constructor
    }

    public ShipmentItemEntity(
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

    public UUID getId() { return id; }
    public String getSku() { return sku; }
    public int getQuantity() { return quantity; }
    public double getWeight() { return weight; }

    public void setSku(String sku) { this.sku = sku; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setWeight(double weight) { this.weight = weight; }
}
