package com.sunny.times.shipments.api;

import com.sunny.times.shipments.api.dto.CreateShipmentRequest;
import com.sunny.times.shipments.api.dto.ShipmentResponse;
import com.sunny.times.shipments.api.dto.UpdateShipmentStatusRequest;
import com.sunny.times.shipments.domain.service.ShipmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping
    public ShipmentResponse createShipment(@Valid @RequestBody CreateShipmentRequest request) {
        return shipmentService.createShipment(request);
    }

    @GetMapping("/{id}")
    public ShipmentResponse getShipment(@PathVariable UUID id) {
        return shipmentService.getShipment(id);
    }

    @PutMapping("/{id}/status")
    public ShipmentResponse updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateShipmentStatusRequest request
    ) {
        return shipmentService.updateShipmentStatus(id, request.status());
    }

}
