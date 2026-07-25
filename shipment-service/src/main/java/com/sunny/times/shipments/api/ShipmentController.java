package com.sunny.times.movement.shipments.api;

import com.sunny.times.movement.shipments.api.dto.CreateShipmentRequest;
import com.sunny.times.movement.shipments.api.dto.ShipmentResponse;
import com.sunny.times.movement.shipments.domain.model.Shipment;
import com.sunny.times.movement.shipments.domain.service.ShipmentService;
import com.sunny.times.movement.shipments.mapper.ShipmentMapper;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final ShipmentMapper shipmentMapper;

    public ShipmentController(ShipmentService shipmentService, ShipmentMapper shipmentMapper) {
        this.shipmentService = shipmentService;
        this.shipmentMapper = shipmentMapper;
    }

    @PostMapping
    public ShipmentResponse createShipment(@Valid @RequestBody CreateShipmentRequest request) {
        Shipment shipment = shipmentService.createShipment(request);
        return shipmentMapper.toResponse(shipment);
    }

    @GetMapping("/{id}")
    public ShipmentResponse getShipment(@PathVariable UUID id) {
        Shipment shipment = shipmentService.getShipment(id);
        return shipmentMapper.toResponse(shipment);
    }
}
