package com.sunny.times.shipments.api;

import com.sunny.times.shipments.api.dto.CreateShipmentRequest;
import com.sunny.times.shipments.api.dto.ShipmentResponse;
import com.sunny.times.shipments.api.dto.UpdateShipmentStatusRequest;
import com.sunny.times.shipments.domain.service.ShipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService service;

    public ShipmentController(ShipmentService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ShipmentResponse>> getAll() {
        List<ShipmentResponse> response = service.getAll()
                .stream()
                .map(ShipmentResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(
                ShipmentResponse.fromDomain(service.getById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ShipmentResponse> create(@RequestBody CreateShipmentRequest req) {
        return ResponseEntity.ok(
                ShipmentResponse.fromDomain(service.create(req))
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ShipmentResponse> updateStatus(
            @PathVariable String id,
            @RequestBody UpdateShipmentStatusRequest req
    ) {
        return ResponseEntity.ok(
                ShipmentResponse.fromDomain(service.updateStatus(id, req.status()))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
