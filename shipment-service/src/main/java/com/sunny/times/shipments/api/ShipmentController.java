package com.sunny.times.shipments.api;

import com.sunny.times.contracts.shipments.CreateShipmentRequest;
import com.sunny.times.contracts.shipments.ShipmentResponse;
import com.sunny.times.contracts.shipments.UpdateShipmentRequest;
import com.sunny.times.contracts.shipments.ProcessShipmentEventRequest;
import com.sunny.times.contracts.shipments.ClientShipmentRequest;
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
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ShipmentResponse> create(@RequestBody CreateShipmentRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShipmentResponse> update(
            @PathVariable String id,
            @RequestBody UpdateShipmentRequest req
    ) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/event")
    public ResponseEntity<ShipmentResponse> processEvent(
            @PathVariable String id,
            @RequestBody ProcessShipmentEventRequest req
    ) {

        return ResponseEntity.ok(
                service.processEvent(
                        id,
                        req.event()
                )
        );
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<?> history(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(
                service.getHistory(id)
        );
    }

    @PostMapping("/requests")
    public ResponseEntity<ShipmentResponse> createRequest(
            @RequestBody ClientShipmentRequest req
    ) {

        return ResponseEntity.ok(
                service.createRequest(req)
        );
    }

    
}
