package com.sunny.times.tracking.api;

import com.sunny.times.contracts.tracking.VehicleLocationDto;
import com.sunny.times.contracts.common.ApiResponse;
import com.sunny.times.tracking.domain.service.TrackingResetService;
import com.sunny.times.tracking.domain.service.VehicleLocationService;
import com.sunny.times.tracking.persistence.entity.VehicleLocationEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/tracking")
public class TrackingController {

    private final VehicleLocationService service;
    private final TrackingResetService trackingResetService;

    public TrackingController(VehicleLocationService service, TrackingResetService trackingResetService) {
        this.service = service;
        this.trackingResetService = trackingResetService;
    }

        @PostMapping("/locations")
    public ApiResponse<VehicleLocationEntity> save(
            @RequestBody VehicleLocationDto dto
    ) {
        if (dto.shipmentId() == null || dto.shipmentId().isBlank()
                || dto.vehicleId() == null || dto.vehicleId().isBlank()) {
            throw new IllegalArgumentException("shipmentId and vehicleId are required.");
        }

        VehicleLocationEntity entity =
                new VehicleLocationEntity(
                        null,
                        dto.shipmentId(),
                        dto.vehicleId(),
                        dto.latitude(),
                        dto.longitude(),
                        Instant.parse(dto.timestamp())
                );

        return new ApiResponse<>(
                "Tracking location saved successfully.",
                service.save(entity)
        );
    }

    @GetMapping("/shipments/{shipmentId}/latest")
    public ResponseEntity<VehicleLocationEntity> latest(
            @PathVariable String shipmentId
    ) {
        return service.getLatest(shipmentId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/shipments/{shipmentId}/history")
    public List<VehicleLocationEntity> history(
            @PathVariable String shipmentId
    ) {
        return service.getHistory(shipmentId);
    }

    @PostMapping("/admin/reset")
    public ApiResponse<Void> reset() {

        trackingResetService.reset();

        return new ApiResponse<>(
                "Tracking data reset successfully.",
                null
        );
    }

}
