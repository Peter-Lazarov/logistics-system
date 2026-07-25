package com.sunny.times.movement.shipments.persistence.repository;

import com.sunny.times.movement.shipments.persistence.entity.ShipmentItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShipmentItemRepository extends JpaRepository<ShipmentItemEntity, UUID> {

}
