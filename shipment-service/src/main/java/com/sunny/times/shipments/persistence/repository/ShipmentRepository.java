package com.sunny.times.shipments.persistence.repository;

import com.sunny.times.shipments.persistence.entity.ShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShipmentRepository extends JpaRepository<ShipmentEntity, UUID> {

}
