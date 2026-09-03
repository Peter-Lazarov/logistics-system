package com.sunny.times.tracking.api.mapper;

import com.sunny.times.tracking.api.dto.TrackingEventResponse;
import com.sunny.times.tracking.persistence.entity.TrackingEventEntity;
import org.springframework.stereotype.Component;

@Component
public class TrackingEventMapper {

    public TrackingEventResponse toResponse(TrackingEventEntity entity) {
        return new TrackingEventResponse(
                entity.getShipmentId(),
                entity.getEventType(),
                entity.getTimestamp(),
                entity.getReason()
        );
    }
}
