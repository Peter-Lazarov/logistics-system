package com.sunny.times.tracking.domain.service;

import com.sunny.times.tracking.persistence.repository.VehicleLocationRepository;
import org.springframework.stereotype.Service;

@Service
public class TrackingResetService {

    private final VehicleLocationRepository repository;

    public TrackingResetService(
            VehicleLocationRepository repository
    ) {
        this.repository = repository;
    }

    public void reset() {

        repository.deleteAll();

    }
}
