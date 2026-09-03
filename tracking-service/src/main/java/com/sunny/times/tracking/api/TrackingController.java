package com.sunny.times.tracking.api;

import com.sunny.times.tracking.api.dto.TrackingEventResponse;
import com.sunny.times.tracking.api.dto.TrackingTimelineResponse;
import com.sunny.times.tracking.api.mapper.TrackingEventMapper;
import com.sunny.times.tracking.domain.service.TrackingEventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tracking/events")
public class TrackingController {

    private final TrackingEventService trackingEventService;
    private final TrackingEventMapper mapper;

    public TrackingController(TrackingEventService trackingEventService,
                              TrackingEventMapper mapper) {
        this.trackingEventService = trackingEventService;
        this.mapper = mapper;
    }

    @GetMapping("/{shipmentId}/latest")
    public TrackingEventResponse getLatest(@PathVariable UUID shipmentId) {
        var latest = trackingEventService.getLatestEvent(shipmentId);
        return latest != null ? mapper.toResponse(latest) : null;
    }

    @GetMapping("/{shipmentId}/timeline")
    public TrackingTimelineResponse getTimeline(@PathVariable UUID shipmentId) {

        var events = trackingEventService.getTimeline(shipmentId)
                .stream()
                .map(mapper::toResponse)
                .toList();

        return new TrackingTimelineResponse(shipmentId, events);
    }
}
