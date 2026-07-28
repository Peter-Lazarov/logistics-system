package com.sunny.times.tracking.domain.service;

import com.sunny.times.tracking.domain.model.TrackingEvent;
import com.sunny.times.tracking.persistence.repository.TrackingEventRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class TrackingEventService {
    private final TrackingEventRepository trackingEventRepository;

    public TrackingEventService(TrackingEventRepository trackingEventRepository){
        this.trackingEventRepository = trackingEventRepository;
    }

    public TrackingEvent recordCreatedEvent(UUID shipmentId){
        TrackingEvent event = new TrackingEvent(
                shipmentId,
                "CREATED",
                Instant.now()
        );

        return trackingEventRepository.save(event);
    }

    public  TrackingEvent saveEvent(TrackingEvent event){
        return trackingEventRepository.save(event);
    }

}
