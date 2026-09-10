package com.sunny.times.shipments.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "shipment_items")
public class ShipmentItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "shipment_id", nullable = false)
    private ShipmentEntity shipment;

    public ShipmentItemEntity() {
    }

    public ShipmentItemEntity(String id, String name, String description, Integer quantity, ShipmentEntity shipment) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.shipment = shipment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ShipmentEntity getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentEntity shipment) {
        this.shipment = shipment;
    }
}
