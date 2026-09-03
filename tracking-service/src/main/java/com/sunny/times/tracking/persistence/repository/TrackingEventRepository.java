package com.sunny.times.tracking.persistence.repository;

import com.sunny.times.tracking.persistence.entity.TrackingEventEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrackingEventRepository extends MongoRepository<TrackingEventEntity, String> {

    List<TrackingEventEntity> findByShipmentId(UUID shipmentId);

    Optional<TrackingEventEntity> findFirstByShipmentIdOrderByTimestampDesc(UUID shipmentId);

}
