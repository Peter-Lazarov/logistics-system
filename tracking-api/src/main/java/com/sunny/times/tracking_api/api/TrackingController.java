package com.sunny.times.tracking_api.api;

import com.sunny.times.contracts.tracking.TrackingPointEvent;
import com.sunny.times.tracking_api.messaging.TrackingProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gps")
public class TrackingController {

    private final TrackingProducer producer;

    public TrackingController(TrackingProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<Void> receive(
            @RequestBody TrackingPointEvent event
    ) {

        producer.publish(event);

        return ResponseEntity.accepted().build();
    }
}
