package com.sunny.times.shipments.persistence.repository;

import com.sunny.times.shipments.persistence.entity.ShipmentItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShipmentItemRepository extends JpaRepository<ShipmentItemEntity, String> {

    List<ShipmentItemEntity> findByShipmentId(String shipmentId);
}
