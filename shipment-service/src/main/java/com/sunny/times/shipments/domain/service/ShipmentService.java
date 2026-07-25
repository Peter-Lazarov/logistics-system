package com.sunny.times.movement.shipments.domain.service;

import com.sunny.times.movement.shipments.api.dto.CreateShipmentRequest;
import com.sunny.times.movement.shipments.domain.model.Shipment;
import com.sunny.times.movement.shipments.mapper.ShipmentMapper;
import com.sunny.times.movement.shipments.persistence.entity.ShipmentEntity;
import com.sunny.times.movement.shipments.persistence.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;

    public ShipmentService(ShipmentRepository shipmentRepository, ShipmentMapper shipmentMapper) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentMapper = shipmentMapper;
    }

    public Shipment createShipment(CreateShipmentRequest request) {
        Shipment domain = shipmentMapper.toDomain(request);
        ShipmentEntity entity = shipmentMapper.toEntity(domain);
        ShipmentEntity saved = shipmentRepository.save(entity);
        return shipmentMapper.toDomain(saved);
    }

    public Shipment getShipment(UUID id) {
        ShipmentEntity entity = shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found: " + id));
        return shipmentMapper.toDomain(entity);
    }
}
