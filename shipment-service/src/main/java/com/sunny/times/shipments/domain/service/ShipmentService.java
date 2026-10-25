package com.sunny.times.shipments.domain.service;

import com.sunny.times.contracts.shipments.ClientShipmentRequest;
import com.sunny.times.contracts.shipments.CreateShipmentRequest;
import com.sunny.times.contracts.shipments.ShipmentResponse;
import com.sunny.times.contracts.shipments.UpdateShipmentRequest;
import com.sunny.times.contracts.tracking.StartSimulationRequest;
import com.sunny.times.contracts.tracking.StopSimulationRequest;
import com.sunny.times.shipments.domain.exception.ShipmentNotFoundException;
import com.sunny.times.shipments.domain.exception.ClientProfileIncompleteException;
import com.sunny.times.shipments.domain.model.Shipment;
import com.sunny.times.shipments.domain.model.ShipmentEvent;
import com.sunny.times.shipments.domain.model.ShipmentStatus;
import com.sunny.times.shipments.domain.model.ShipmentStatusFlow;
import com.sunny.times.shipments.persistence.entity.ShipmentEntity;
import com.sunny.times.shipments.persistence.repository.ShipmentRepository;
import com.sunny.times.shipments.persistence.entity.ShipmentStatusHistoryEntity;
import com.sunny.times.shipments.persistence.repository.ShipmentStatusHistoryRepository;
import com.sunny.times.shipments.mapper.ShipmentMapper;
import com.sunny.times.shipments.common.CommonClientService;

import com.sunny.times.security.CurrentUserService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ShipmentService {

    private final ShipmentRepository repository;
    private final ShipmentMapper mapper;
    private final CommonClientService commonClient;
    private final ShipmentStatusHistoryRepository historyRepository;
    private final CurrentUserService currentUserService;
    private final MockGpsTrackerClient mockGpsTrackerClient;
    private final ShipmentPricingService shipmentPricingService;

    public ShipmentService(
            ShipmentRepository repository,
            ShipmentMapper mapper,
            CommonClientService commonClient,
            ShipmentStatusHistoryRepository historyRepository,
            CurrentUserService currentUserService,
            MockGpsTrackerClient mockGpsTrackerClient,
            ShipmentPricingService shipmentPricingService
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.commonClient = commonClient;
        this.historyRepository = historyRepository;
        this.currentUserService = currentUserService;
        this.mockGpsTrackerClient = mockGpsTrackerClient;
        this.shipmentPricingService = shipmentPricingService;
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

        Shipment shipment = new Shipment(
                generateShipmentId(),
                req.category(),
                req.description(),
                req.origin(),
                req.destination(),
                req.clientUserId(),
                req.driverUserId(),
                req.vehicleId(),
                req.simulationRoute(),
                req.totalWeight(),
                req.totalVolume(),
                null,
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
                        currentUserEmail(),
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
        boolean calculateMissingAssignedPrice = entity.getStatus() == ShipmentStatus.ASSIGNED
                && (entity.getSimulationRoute() == null || entity.getSimulationRoute().isBlank())
                && (entity.getDistanceKm() == null || entity.getDistanceKm() == 0.0)
                                && ((req.simulationRoute() != null && !req.simulationRoute().isBlank())
                                        || req.distanceKm() != null);
        entity.setCategory(req.category());
        entity.setDescription(req.description());
        entity.setOrigin(req.origin());
        entity.setDestination(req.destination());
        entity.setClientUserId(req.clientUserId());
        entity.setDriverUserId(req.driverUserId());
        entity.setVehicleId(req.vehicleId());
        entity.setSimulationRoute(req.simulationRoute());
                if (req.distanceKm() != null) {
                        if (req.distanceKm() < 0) {
                                throw new IllegalArgumentException("Distance cannot be negative.");
                        }
                        entity.setDistanceKm(req.distanceKm());
                }
        entity.setTotalWeight(req.totalWeight());
        entity.setTotalVolume(req.totalVolume());
                if (calculateMissingAssignedPrice) {
                        shipmentPricingService.calculateAndStore(entity);
                }

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
                s.getSimulationRoute(),
                s.getTotalWeight(),
                s.getTotalVolume(),
                s.getDistanceKm(),
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
        ShipmentStatus currentStatus = entity.getStatus();

        ShipmentEvent event =
                ShipmentEvent.valueOf(eventName);

        ShipmentStatus nextStatus =
                ShipmentStatusFlow.nextStatus(
                        currentStatus,
                        event
                );

        if (currentStatus == ShipmentStatus.CREATED
                && nextStatus == ShipmentStatus.ASSIGNED) {
                        shipmentPricingService.calculateAndStore(entity);
        }

        entity.setStatus(nextStatus);

        entity.setUpdatedAt(
                Instant.now()
        );

        historyRepository.save(
                new ShipmentStatusHistoryEntity(
                        null,
                        entity.getId(),
                        nextStatus,
                        currentUserEmail(),
                        Instant.now()
                )
        );

        ShipmentEntity saved =
                repository.save(entity);

        if (nextStatus == ShipmentStatus.IN_TRANSIT
                && (currentStatus == ShipmentStatus.LOADED
                    || currentStatus == ShipmentStatus.DELAYED)
                && entity.getVehicleId() != null
                && !entity.getVehicleId().isBlank()) {
            mockGpsTrackerClient.startSimulation(new StartSimulationRequest(
                    entity.getId(),
                    entity.getVehicleId(),
                    entity.getSimulationRoute()
            ));
        }

        if (currentStatus == ShipmentStatus.IN_TRANSIT
                && nextStatus == ShipmentStatus.ARRIVED) {
            mockGpsTrackerClient.stopSimulation(new StopSimulationRequest(entity.getId()));
        }

        return toResponse(
                mapper.toDomain(saved)
        );
    }

    public ShipmentResponse createRequest(ClientShipmentRequest req) {

        Long clientUserId = currentUserService.getCurrentUser().userId();
                var client = commonClient.getClient(clientUserId);
                if (client.companyName() == null || client.companyName().isBlank()
                                || client.companyAddress() == null || client.companyAddress().isBlank()
                                || client.vatNumber() == null || client.vatNumber().isBlank()) {
                        throw new ClientProfileIncompleteException();
                }

        Shipment shipment = new Shipment(
                generateShipmentId(),
                req.category(),
                req.description(),
                req.origin(),
                req.destination(),

                clientUserId,

                null,
                null,
                req.simulationRoute(),

                req.totalWeight(),
                req.totalVolume(),
                null,

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
                        currentUserEmail(),
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

    private String currentUserEmail() {
        return currentUserService
                .getCurrentUser()
                .email();
    }

}
