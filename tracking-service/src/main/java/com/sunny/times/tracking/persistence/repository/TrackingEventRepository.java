package com.sunny.times.tracking.persistence.repository;

import com.sunny.times.tracking.persistence.entity.TrackingEventEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface TrackingEventRepository extends MongoRepository<TrackingEventEntity, String> {

    List<TrackingEventEntity> findByShipmentId(UUID shipmentId);
}
