package com.sunny.times.tracking.domain.service;

import com.sunny.times.tracking.persistence.entity.VehicleLocationEntity;
import com.sunny.times.tracking.persistence.repository.VehicleLocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public VehicleLocationEntity getLatest(
            String vehicleId
    ) {
        return repository
                .findFirstByVehicleIdOrderByTimestampDesc(vehicleId)
                .orElse(null);
    }

    public List<VehicleLocationEntity> getHistory(
            String vehicleId
    ) {
        return repository
                .findByVehicleIdOrderByTimestampAsc(vehicleId);
    }
}