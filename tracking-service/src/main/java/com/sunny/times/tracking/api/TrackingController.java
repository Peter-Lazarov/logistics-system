package com.sunny.times.tracking.api;

import com.sunny.times.tracking.domain.service.TrackingEventService;
import com.sunny.times.tracking.messaging.event.ShipmentCreatedEvent;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tracking/events")
public class TrackingController {
    private final TrackingEventService trackingEventService;

    public TrackingController(TrackingEventService trackingEventService){
        this.trackingEventService = trackingEventService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void handleShipmentCreated(@RequestBody ShipmentCreatedEvent event) {
        trackingEventService.recordCreatedEvent(event.getShipmentId());
    }
}
