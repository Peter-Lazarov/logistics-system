package com.sunny.times.tracking.api.mapper;

import com.sunny.times.tracking.api.dto.TrackingEventResponse;
import com.sunny.times.tracking.persistence.entity.TrackingEventEntity;

public class TrackingEventMapper {

    public static TrackingEventResponse toResponse(TrackingEventEntity entity) {
        return new TrackingEventResponse(
                entity.getEventType(),
                entity.getTimestamp(),
                entity.getReason()
        );
    }
}
