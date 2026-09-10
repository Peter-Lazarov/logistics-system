package com.sunny.times.shipments.domain.service;

import com.sunny.times.shipments.api.dto.CreateShipmentRequest;
import com.sunny.times.shipments.api.dto.ShipmentItemDto;
import com.sunny.times.shipments.api.dto.ShipmentStatusDto;
import com.sunny.times.shipments.domain.exception.ShipmentNotFoundException;
import com.sunny.times.shipments.domain.model.Shipment;
import com.sunny.times.shipments.domain.model.ShipmentItem;
import com.sunny.times.shipments.domain.model.ShipmentStatus;
import com.sunny.times.shipments.mapper.ShipmentMapper;
import com.sunny.times.shipments.persistence.entity.ShipmentEntity;
import com.sunny.times.shipments.persistence.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ShipmentService {

    private final ShipmentRepository repository;
    private final ShipmentMapper mapper;

    public ShipmentService(ShipmentRepository repository, ShipmentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<Shipment> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    public Shipment getById(String id) {
        ShipmentEntity entity = repository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException(id));
        return mapper.toDomain(entity);
    }

    public Shipment create(CreateShipmentRequest request) {

        List<ShipmentItem> items = request.getItems().stream()
                .map(i -> new ShipmentItem(
                        null,
                        i.getName(),
                        i.getDescription(),
                        i.getQuantity()
                ))
                .toList();

        Shipment shipment = new Shipment(
                UUID.randomUUID().toString(),
                request.getType(),
                request.getQuantity(),
                request.getWeight(),
                request.getVolume(),
                ShipmentStatus.CREATED,
                request.getOrigin(),
                request.getDestination(),
                request.getVehicleId(),
                request.getDriverId(),
                request.getPathId(),
                null,
                Instant.now(),
                Instant.now(),
                items
        );

        ShipmentEntity saved = repository.save(mapper.toEntity(shipment));
        return mapper.toDomain(saved);
    }

    public Shipment updateStatus(String id, ShipmentStatusDto newStatus) {
        ShipmentEntity entity = repository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException(id));

        entity.setStatus(ShipmentStatus.valueOf(newStatus.name()));
        entity.setUpdatedAt(Instant.now());

        ShipmentEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ShipmentNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
