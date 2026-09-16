package com.sunny.times.shipments.domain.service;

import com.sunny.times.contracts.shipments.CreateShipmentRequest;
import com.sunny.times.contracts.shipments.ShipmentResponse;
import com.sunny.times.contracts.shipments.ShipmentStatusDto;
import com.sunny.times.shipments.common.CommonClientService;
import com.sunny.times.shipments.domain.exception.ShipmentNotFoundException;
import com.sunny.times.shipments.domain.model.Shipment;
import com.sunny.times.shipments.domain.model.ShipmentStatus;
import com.sunny.times.shipments.mapper.ShipmentMapper;
import com.sunny.times.shipments.persistence.entity.ShipmentEntity;
import com.sunny.times.shipments.persistence.repository.ShipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ShipmentServiceTest {

    private ShipmentRepository repository;
    private ShipmentMapper mapper;
    private CommonClientService commonClient;
    private ShipmentService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(ShipmentRepository.class);
        mapper = Mockito.mock(ShipmentMapper.class);
        commonClient = Mockito.mock(CommonClientService.class);
        service = new ShipmentService(repository, mapper, commonClient);
    }

    @Test
    void create_shouldSaveShipment() {

        CreateShipmentRequest req = new CreateShipmentRequest(
                "Wood",
                "Oak planks",
                "Sofia",
                "Plovdiv",
                "CL-001",
                "DR-001",
                "CB1234AB",
                "1102",
                10000.0,
                12.5,
                120.50
        );

        Shipment domain = new Shipment(
                UUID.randomUUID().toString(),
                "Wood",
                "Oak planks",
                "Sofia",
                "Plovdiv",
                "CL-001",
                "DR-001",
                "CB1234AB",
                "1102",
                10000.0,
                12.5,
                120.50,
                ShipmentStatus.CREATED,
                Instant.now(),
                Instant.now()
        );

        ShipmentEntity entity = new ShipmentEntity(
                domain.getId(),
                domain.getCategory(),
                domain.getDescription(),
                domain.getOrigin(),
                domain.getDestination(),
                domain.getClientId(),
                domain.getDriverId(),
                domain.getVehicleId(),
                domain.getPathId(),
                domain.getTotalWeight(),
                domain.getTotalVolume(),
                domain.getPrice(),
                domain.getStatus(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );

        ShipmentResponse response = new ShipmentResponse(
                domain.getId(),
                domain.getCategory(),
                domain.getDescription(),
                domain.getOrigin(),
                domain.getDestination(),
                domain.getClientId(),
                domain.getDriverId(),
                domain.getVehicleId(),
                domain.getPathId(),
                domain.getTotalWeight(),
                domain.getTotalVolume(),
                domain.getPrice(),
                domain.getStatus().name(),
                domain.getCreatedAt().toString(),
                domain.getUpdatedAt().toString()
        );

        when(mapper.toEntity(any())).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        ShipmentResponse result = service.create(req);

        assertNotNull(result);
        assertEquals("Sofia", result.origin());
        assertEquals("Plovdiv", result.destination());
        verify(repository, times(1)).save(any());
    }

    @Test
    void getById_shouldReturnShipment() {

        ShipmentEntity entity = new ShipmentEntity(
                "SHP-10001",
                "Wood",
                "Oak planks",
                "Sofia",
                "Plovdiv",
                "CL-001",
                "DR-001",
                "CB1234AB",
                "1102",
                10000.0,
                12.5,
                120.50,
                ShipmentStatus.CREATED,
                Instant.now(),
                Instant.now()
        );

        Shipment domain = new Shipment(
                "SHP-10001",
                "Wood",
                "Oak planks",
                "Sofia",
                "Plovdiv",
                "CL-001",
                "DR-001",
                "CB1234AB",
                "1102",
                10000.0,
                12.5,
                120.50,
                ShipmentStatus.CREATED,
                Instant.now(),
                Instant.now()
        );

        ShipmentResponse response = new ShipmentResponse(
                domain.getId(),
                domain.getCategory(),
                domain.getDescription(),
                domain.getOrigin(),
                domain.getDestination(),
                domain.getClientId(),
                domain.getDriverId(),
                domain.getVehicleId(),
                domain.getPathId(),
                domain.getTotalWeight(),
                domain.getTotalVolume(),
                domain.getPrice(),
                domain.getStatus().name(),
                domain.getCreatedAt().toString(),
                domain.getUpdatedAt().toString()
        );

        when(repository.findById("SHP-10001")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        ShipmentResponse result = service.getById("SHP-10001");

        assertEquals("SHP-10001", result.id());
        assertEquals("Sofia", result.origin());
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(repository.findById("missing")).thenReturn(Optional.empty());
        assertThrows(ShipmentNotFoundException.class, () -> service.getById("missing"));
    }

    @Test
    void updateStatus_shouldUpdateShipmentStatus() {

        ShipmentEntity entity = new ShipmentEntity(
                "SHP-10001",
                "Wood",
                "Oak planks",
                "Sofia",
                "Plovdiv",
                "CL-001",
                "DR-001",
                "CB1234AB",
                "1102",
                10000.0,
                12.5,
                120.50,
                ShipmentStatus.CREATED,
                Instant.now(),
                Instant.now()
        );

        Shipment domain = new Shipment(
                "SHP-10001",
                "Wood",
                "Oak planks",
                "Sofia",
                "Plovdiv",
                "CL-001",
                "DR-001",
                "CB1234AB",
                "1102",
                10000.0,
                12.5,
                120.50,
                ShipmentStatus.DELIVERED,
                Instant.now(),
                Instant.now()
        );

        ShipmentResponse response = new ShipmentResponse(
                domain.getId(),
                domain.getCategory(),
                domain.getDescription(),
                domain.getOrigin(),
                domain.getDestination(),
                domain.getClientId(),
                domain.getDriverId(),
                domain.getVehicleId(),
                domain.getPathId(),
                domain.getTotalWeight(),
                domain.getTotalVolume(),
                domain.getPrice(),
                domain.getStatus().name(),
                domain.getCreatedAt().toString(),
                domain.getUpdatedAt().toString()
        );

        when(repository.findById("SHP-10001")).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        ShipmentResponse result = service.updateStatus("SHP-10001", ShipmentStatusDto.DELIVERED);

        assertEquals("DELIVERED", result.status());
    }

    @Test
    void delete_shouldRemoveShipment() {
        when(repository.existsById("SHP-10001")).thenReturn(true);
        doNothing().when(repository).deleteById("SHP-10001");

        service.delete("SHP-10001");

        verify(repository, times(1)).deleteById("SHP-10001");
    }

    @Test
    void delete_shouldThrowException_whenNotFound() {
        when(repository.existsById("missing")).thenReturn(false);
        assertThrows(ShipmentNotFoundException.class, () -> service.delete("missing"));
    }
}
