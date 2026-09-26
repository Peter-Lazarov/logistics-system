package com.sunny.times.shipments.persistence.repository;

import com.sunny.times.shipments.persistence.entity.ShipmentStatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentStatusHistoryRepository
        extends JpaRepository<ShipmentStatusHistoryEntity, Long> {

    List<ShipmentStatusHistoryEntity>
    findByShipmentIdOrderByChangedAtAsc(
            String shipmentId
    );
}