package com.sunny.times.workflow.client;

import com.sunny.times.contracts.tracking.TrackingEventResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "tracking-service",
        url = "${tracking.url}"
)
public interface TrackingClient {

    @GetMapping("/tracking/events/{shipmentId}/latest")
    TrackingEventResponse getLatest(@PathVariable UUID shipmentId);
}
