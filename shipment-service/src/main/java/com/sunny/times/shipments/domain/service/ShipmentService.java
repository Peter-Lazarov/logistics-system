package com.sunny.times.shipments.domain.service;

import com.sunny.times.contracts.shipments.ClientShipmentRequest;
import com.sunny.times.contracts.shipments.CreateShipmentRequest;
import com.sunny.times.contracts.shipments.ShipmentResponse;
import com.sunny.times.contracts.shipments.UpdateShipmentRequest;
import com.sunny.times.shipments.common.CommonClientService;
import com.sunny.times.shipments.domain.exception.ShipmentNotFoundException;
import com.sunny.times.shipments.domain.model.Shipment;
import com.sunny.times.shipments.domain.model.ShipmentEvent;
import com.sunny.times.shipments.domain.model.ShipmentStatus;
import com.sunny.times.shipments.domain.model.ShipmentStatusFlow;
import com.sunny.times.shipments.mapper.ShipmentMapper;
import com.sunny.times.shipments.persistence.entity.ShipmentEntity;
import com.sunny.times.shipments.persistence.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import com.sunny.times.shipments.persistence.entity.ShipmentStatusHistoryEntity;
import com.sunny.times.shipments.persistence.repository.ShipmentStatusHistoryRepository;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.List;

@Service
public class ShipmentService {

    private final ShipmentRepository repository;
    private final ShipmentMapper mapper;
    private final CommonClientService commonClient;
    private final ShipmentStatusHistoryRepository historyRepository;

    public ShipmentService(
            ShipmentRepository repository,
            ShipmentMapper mapper,
            CommonClientService commonClient,
            ShipmentStatusHistoryRepository historyRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.commonClient = commonClient;
        this.historyRepository = historyRepository;
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
        commonClient.getClient(req.clientUserId());

        if (req.driverUserId() != null) {
            commonClient.getDriver(
                    req.driverUserId()
            );
        }

        if (req.pathId() != null &&
                !req.pathId().isBlank()) {
            commonClient.getRoute(
                    req.pathId()
            );
        }

        Shipment shipment = new Shipment(
                generateShipmentId(),
                req.category(),
                req.description(),
                req.origin(),
                req.destination(),
                req.clientUserId(),
                req.driverUserId(),
                req.vehicleId(),
                req.pathId(),
                req.totalWeight(),
                req.totalVolume(),
                req.price(),
                ShipmentStatus.CREATED,
                Instant.now(),
                Instant.now()
        );

        ShipmentEntity saved =
                repository.save(
                        mapper.toEntity(shipment)
                );

        historyRepository.save(
                new ShipmentStatusHistoryEntity(
                        null,
                        saved.getId(),
                        ShipmentStatus.CREATED,
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName(),
                        Instant.now()
                )
        );

        return toResponse(
                mapper.toDomain(saved)
        );
    }

    public ShipmentResponse update(String id, UpdateShipmentRequest req) {
        ShipmentEntity entity = repository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException(id));

        if (entity.getStatus() != ShipmentStatus.CREATED && entity.getStatus() != ShipmentStatus.ASSIGNED) {
            throw new IllegalStateException(
                    "Shipment can only be edited in CREATED or ASSIGNED status"
            );
        }

        commonClient.getClient(req.clientUserId());
        commonClient.getDriver(req.driverUserId());
        commonClient.getRoute(req.pathId());

        entity.setCategory(req.category());
        entity.setDescription(req.description());
        entity.setOrigin(req.origin());
        entity.setDestination(req.destination());
        entity.setClientUserId(req.clientUserId());
        entity.setDriverUserId(req.driverUserId());
        entity.setVehicleId(req.vehicleId());
        entity.setPathId(req.pathId());
        entity.setTotalWeight(req.totalWeight());
        entity.setTotalVolume(req.totalVolume());
        entity.setPrice(req.price());

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
                s.getClientUserId(),
                s.getDriverUserId(),
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

    public List<ShipmentStatusHistoryEntity> getHistory(
            String shipmentId
    ) {
        return historyRepository
                .findByShipmentIdOrderByChangedAtAsc(
                        shipmentId
                );
    }

    public ShipmentResponse processEvent(
            String id,
            String eventName
    ) {

        ShipmentEntity entity = repository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException(id));

        ShipmentEvent event =
                ShipmentEvent.valueOf(eventName);

        ShipmentStatus nextStatus =
                ShipmentStatusFlow.nextStatus(
                        entity.getStatus(),
                        event
                );

        entity.setStatus(nextStatus);

        entity.setUpdatedAt(
                Instant.now()
        );

        historyRepository.save(
                new ShipmentStatusHistoryEntity(
                        null,
                        entity.getId(),
                        nextStatus,
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName(),
                        Instant.now()
                )
        );

        ShipmentEntity saved =
                repository.save(entity);

        return toResponse(
                mapper.toDomain(saved)
        );
    }

    public ShipmentResponse createRequest(
            ClientShipmentRequest req
    ) {
        Shipment shipment = new Shipment(
                generateShipmentId(),
                req.category(),
                req.description(),
                req.origin(),
                req.destination(),

                1004L,

                null,
                null,
                null,

                req.totalWeight(),
                req.totalVolume(),

                0.0,

                ShipmentStatus.CREATED,

                Instant.now(),
                Instant.now()
        );

        ShipmentEntity saved =
                repository.save(
                        mapper.toEntity(shipment)
                );

        historyRepository.save(
                new ShipmentStatusHistoryEntity(
                        null,
                        saved.getId(),
                        ShipmentStatus.CREATED,
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName(),
                        Instant.now()
                )
        );

        return toResponse(
                mapper.toDomain(saved)
        );
    }

    public List<ShipmentResponse> getMyShipments(Long clientUserId) {

        return repository.findByClientUserId(clientUserId)
                .stream()
                .map(mapper::toDomain)
                .map(this::toResponse)
                .toList();
    }

}
