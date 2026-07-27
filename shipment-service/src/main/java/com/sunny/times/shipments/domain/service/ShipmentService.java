package com.sunny.times.shipments.domain.service;

import com.sunny.times.shipments.api.dto.CreateShipmentRequest;
import com.sunny.times.shipments.api.dto.ShipmentResponse;
import com.sunny.times.shipments.domain.model.Shipment;
import com.sunny.times.shipments.mapper.ShipmentMapper;
import com.sunny.times.shipments.messaging.event.ShipmentCreatedEvent;
import com.sunny.times.shipments.messaging.publisher.ShipmentEventPublisher;
import com.sunny.times.shipments.persistence.entity.ShipmentEntity;
import com.sunny.times.shipments.persistence.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;
    private final ShipmentEventPublisher eventPublisher;

    public ShipmentService(ShipmentRepository shipmentRepository, ShipmentMapper shipmentMapper,
                           ShipmentEventPublisher eventPublisher) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentMapper = shipmentMapper;
        this.eventPublisher = eventPublisher;
    }

    public ShipmentResponse createShipment(CreateShipmentRequest request) {
        Shipment domain = shipmentMapper.toDomain(request);
        ShipmentEntity entity = shipmentMapper.toEntity(domain);
        ShipmentEntity savedEntity = shipmentRepository.save(entity);
        Shipment savedDomain = shipmentMapper.toDomain(savedEntity);

        eventPublisher.publishShipmentCreated(
                new ShipmentCreatedEvent(
                        savedEntity.getId(),
                        savedEntity.getOrderId(),
                        savedEntity.getCreatedAt()
                )
        );

        return shipmentMapper.toResponse(savedDomain);
    }

    public ShipmentResponse getShipment(UUID id) {
        ShipmentEntity entity = shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found: " + id));

        Shipment domain = shipmentMapper.toDomain(entity);
        return shipmentMapper.toResponse(domain);
    }
}

