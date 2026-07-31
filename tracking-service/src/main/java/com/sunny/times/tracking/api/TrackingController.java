package com.sunny.times.tracking.api;

import com.sunny.times.tracking.api.dto.TrackingEventResponse;
import com.sunny.times.tracking.api.dto.TrackingTimelineResponse;
import com.sunny.times.tracking.api.mapper.TrackingEventMapper;
import com.sunny.times.tracking.persistence.entity.TrackingEventEntity;
import com.sunny.times.tracking.persistence.repository.TrackingEventRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tracking/events")
public class TrackingController {

    private final TrackingEventRepository trackingEventRepository;

    public TrackingController(TrackingEventRepository trackingEventRepository){
        this.trackingEventRepository = trackingEventRepository;
    }

    @GetMapping("/{shipmentId}")
    public TrackingTimelineResponse getTracking(@PathVariable UUID shipmentId) {

        List<TrackingEventEntity> events = trackingEventRepository.findByShipmentId(shipmentId);

        events.sort(Comparator.comparing(TrackingEventEntity::getTimestamp));

        List<TrackingEventResponse> responses = events.stream()
                .map(TrackingEventMapper::toResponse)
                .toList();

        return new TrackingTimelineResponse(shipmentId, responses);
    }
}
