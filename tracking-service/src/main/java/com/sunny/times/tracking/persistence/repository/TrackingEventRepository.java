package com.sunny.times.tracking.persistence.repository;

import com.sunny.times.tracking.domain.model.TrackingEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrackingEventRepository extends MongoRepository<TrackingEvent, String> {

}
