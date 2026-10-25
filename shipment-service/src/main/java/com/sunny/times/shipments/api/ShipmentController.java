package com.sunny.times.shipments.api;

import com.sunny.times.contracts.shipments.CreateShipmentRequest;
import com.sunny.times.contracts.shipments.ShipmentResponse;
import com.sunny.times.contracts.shipments.UpdateShipmentRequest;
import com.sunny.times.contracts.shipments.ProcessShipmentEventRequest;
import com.sunny.times.contracts.shipments.ClientShipmentRequest;
import com.sunny.times.contracts.common.ApiMessage;
import com.sunny.times.contracts.common.ApiResponse;
import com.sunny.times.security.CurrentUserService;
import com.sunny.times.shipments.domain.service.ShipmentResetService;
import com.sunny.times.shipments.domain.service.ShipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService service;
    private final CurrentUserService currentUserService;
    private final ShipmentResetService shipmentResetService;

    public ShipmentController(ShipmentService service, CurrentUserService currentUserService, ShipmentResetService shipmentResetService) {
        this.service = service;
        this.currentUserService = currentUserService;
        this.shipmentResetService = shipmentResetService;
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
    public ResponseEntity<ApiResponse<ShipmentResponse>> create(@RequestBody CreateShipmentRequest req) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Shipment created successfully.",
                service.create(req)
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ShipmentResponse>> update(
            @PathVariable String id,
            @RequestBody UpdateShipmentRequest req
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Shipment updated successfully.",
                service.update(id, req)
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiMessage> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok(new ApiMessage("Shipment deleted successfully."));
    }

    @PatchMapping("/{id}/event")
    public ResponseEntity<ApiResponse<ShipmentResponse>> processEvent(
            @PathVariable String id,
            @RequestBody ProcessShipmentEventRequest req
    ) {

        return ResponseEntity.ok(new ApiResponse<>(
                "Shipment status updated successfully.",
                service.processEvent(id, req.event())
        ));
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
    public ResponseEntity<ApiResponse<ShipmentResponse>> createRequest(
            @RequestBody ClientShipmentRequest req
    ) {

        return ResponseEntity.ok(new ApiResponse<>(
                "Shipment created successfully.",
                service.createRequest(req)
        ));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ShipmentResponse>> getMyShipments() {

        Long userId =
                currentUserService
                        .getCurrentUser()
                        .userId();

        return ResponseEntity.ok(
                service.getMyShipments(userId)
        );
    }

    @PostMapping("/admin/reset")
    public ApiResponse<Void> reset() {

        shipmentResetService.reset();

        return new ApiResponse<>(
                "Shipment data reset successfully.",
                null
        );
    }

}
