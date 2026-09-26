package com.sunny.times.tracking.persistence.repository;

import com.sunny.times.tracking.persistence.entity.VehicleLocationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleLocationRepository
        extends MongoRepository<VehicleLocationEntity, String> {

    Optional<VehicleLocationEntity>
    findFirstByVehicleIdOrderByTimestampDesc(String vehicleId);

    List<VehicleLocationEntity>
    findByVehicleIdOrderByTimestampAsc(String vehicleId);
}

