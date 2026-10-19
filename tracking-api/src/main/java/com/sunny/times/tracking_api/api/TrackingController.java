package com.sunny.times.tracking_api.api;

import com.sunny.times.contracts.tracking.TrackingPointEvent;
import com.sunny.times.contracts.common.ApiMessage;
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
    public ResponseEntity<ApiMessage> receive(
            @RequestBody TrackingPointEvent event
    ) {

        producer.publish(event);

        return ResponseEntity.accepted()
                .body(new ApiMessage("Location update accepted."));
    }
}
