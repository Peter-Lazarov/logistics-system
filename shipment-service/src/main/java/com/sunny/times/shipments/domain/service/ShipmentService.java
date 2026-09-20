package com.sunny.times.shipments.domain.service;

import com.sunny.times.contracts.shipments.CreateShipmentRequest;
import com.sunny.times.contracts.shipments.ShipmentResponse;
import com.sunny.times.contracts.shipments.ShipmentStatusDto;
import com.sunny.times.contracts.shipments.UpdateShipmentRequest;
import com.sunny.times.shipments.common.CommonClientService;
import com.sunny.times.shipments.domain.exception.ShipmentNotFoundException;
import com.sunny.times.shipments.domain.model.Shipment;
import com.sunny.times.shipments.domain.model.ShipmentStatus;
import com.sunny.times.shipments.mapper.ShipmentMapper;
import com.sunny.times.shipments.persistence.entity.ShipmentEntity;
import com.sunny.times.shipments.persistence.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ShipmentService {

    private final ShipmentRepository repository;
    private final ShipmentMapper mapper;
    private final CommonClientService commonClient;

    public ShipmentService(ShipmentRepository repository,
                           ShipmentMapper mapper,
                           CommonClientService commonClient) {
        this.repository = repository;
        this.mapper = mapper;
        this.commonClient = commonClient;
    }

    public List<ShipmentResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .map(this::toResponse)
                .toList();
    }

    public ShipmentResponse getById(String id) {
        ShipmentEntity entity = repository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException(id));

        return toResponse(mapper.toDomain(entity));
    }

    public ShipmentResponse create(CreateShipmentRequest req) {

        commonClient.getClient(req.clientId());
        commonClient.getDriver(req.driverId());
        commonClient.getRoute(req.pathId());

        Shipment shipment = new Shipment(
                generateShipmentId(),
                req.category(),
                req.description(),
                req.origin(),
                req.destination(),
                req.clientId(),
                req.driverId(),
                req.vehicleId(),
                req.pathId(),
                req.totalWeight(),
                req.totalVolume(),
                req.price(),
                ShipmentStatus.CREATED,
                Instant.now(),
                Instant.now()
        );

        ShipmentEntity saved = repository.save(mapper.toEntity(shipment));
        return toResponse(mapper.toDomain(saved));
    }

    public ShipmentResponse update(String id, UpdateShipmentRequest req) {


        ShipmentEntity entity = repository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException(id));

        if (entity.getStatus() != ShipmentStatus.CREATED
                && entity.getStatus() != ShipmentStatus.ASSIGNED) {

            throw new IllegalStateException(
                    "Shipment can only be edited in CREATED or ASSIGNED status"
            );
        }

        commonClient.getClient(req.clientId());
        commonClient.getDriver(req.driverId());
        commonClient.getRoute(req.pathId());

        entity.setCategory(req.category());
        entity.setDescription(req.description());
        entity.setOrigin(req.origin());
        entity.setDestination(req.destination());
        entity.setClientId(req.clientId());
        entity.setDriverId(req.driverId());
        entity.setVehicleId(req.vehicleId());
        entity.setPathId(req.pathId());
        entity.setTotalWeight(req.totalWeight());
        entity.setTotalVolume(req.totalVolume());
        entity.setPrice(req.price());

        entity.setUpdatedAt(Instant.now());

        ShipmentEntity saved = repository.save(entity);

        return toResponse(mapper.toDomain(saved));
    }

    public ShipmentResponse updateStatus(String id, ShipmentStatusDto newStatus) {
        ShipmentEntity entity = repository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException(id));

        entity.setStatus(ShipmentStatus.valueOf(newStatus.name()));
        entity.setUpdatedAt(Instant.now());

        ShipmentEntity saved = repository.save(entity);
        return toResponse(mapper.toDomain(saved));
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ShipmentNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private String generateShipmentId() {

        long nextNumber = repository.count() + 10001;

        return "SHP-" + nextNumber;
    }

    private ShipmentResponse toResponse(Shipment s) {
        return new ShipmentResponse(
                s.getId(),
                s.getCategory(),
                s.getDescription(),
                s.getOrigin(),
                s.getDestination(),
                s.getClientId(),
                s.getDriverId(),
                s.getVehicleId(),
                s.getPathId(),
                s.getTotalWeight(),
                s.getTotalVolume(),
                s.getPrice(),
                s.getStatus().name(),
                s.getCreatedAt().toString(),
                s.getUpdatedAt().toString()
        );
    }

}
