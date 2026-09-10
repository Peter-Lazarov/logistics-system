package com.sunny.times.shipments.persistence.repository;

import com.sunny.times.shipments.persistence.entity.ShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShipmentRepository extends JpaRepository<ShipmentEntity, String> {
}
