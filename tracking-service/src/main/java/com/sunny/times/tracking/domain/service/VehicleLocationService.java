package com.sunny.times.tracking.domain.service;

import com.sunny.times.tracking.persistence.entity.VehicleLocationEntity;
import com.sunny.times.tracking.persistence.repository.VehicleLocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleLocationService {

    private final VehicleLocationRepository repository;

    public VehicleLocationService(
            VehicleLocationRepository repository
    ) {
        this.repository = repository;
    }

    public VehicleLocationEntity save(
            VehicleLocationEntity location
    ) {
        return repository.save(location);
    }

    public Optional<VehicleLocationEntity> getLatest(
            String shipmentId
    ) {
        return repository.findTopByShipmentIdOrderByTimestampDesc(shipmentId);
    }

    public List<VehicleLocationEntity> getHistory(
            String shipmentId
    ) {
        return repository
                .findByShipmentIdOrderByTimestampAsc(shipmentId);
    }
}