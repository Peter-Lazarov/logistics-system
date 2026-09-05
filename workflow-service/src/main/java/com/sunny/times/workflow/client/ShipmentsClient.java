package com.sunny.times.workflow.client;

import com.sunny.times.contracts.shipments.ShipmentResponse;
import com.sunny.times.contracts.shipments.UpdateShipmentStatusRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "shipment-service",
        url = "${shipments.url}"
)
public interface ShipmentsClient {

    @PutMapping("/shipments/{id}/status")
    ShipmentResponse updateStatus(
            @PathVariable UUID id,
            @RequestBody UpdateShipmentStatusRequest request
    );

    @GetMapping("/shipments/{id}")
    ShipmentResponse getShipment(@PathVariable UUID id);

}
