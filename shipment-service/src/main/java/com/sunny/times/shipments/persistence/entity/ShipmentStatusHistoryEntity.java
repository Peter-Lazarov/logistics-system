package com.sunny.times.shipments.persistence.entity;

import com.sunny.times.shipments.domain.model.ShipmentStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "shipment_status_history")
public class ShipmentStatusHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String shipmentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipmentStatus status;

    @Column(nullable = false)
    private String changedBy;

    @Column(nullable = false)
    private Instant changedAt;

    public ShipmentStatusHistoryEntity() {
    }

    public ShipmentStatusHistoryEntity(
            Long id,
            String shipmentId,
            ShipmentStatus status,
            String changedBy,
            Instant changedAt
    ) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.status = status;
        this.changedBy = changedBy;
        this.changedAt = changedAt;
    }

    public Long getId() {
        return id;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public Instant getChangedAt() {
        return changedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public void setChangedAt(Instant changedAt) {
        this.changedAt = changedAt;
    }
}
