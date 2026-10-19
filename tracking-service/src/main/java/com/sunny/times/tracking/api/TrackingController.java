package com.sunny.times.tracking.api;

import com.sunny.times.contracts.tracking.VehicleLocationDto;
import com.sunny.times.contracts.common.ApiResponse;
import com.sunny.times.tracking.domain.service.VehicleLocationService;
import com.sunny.times.tracking.persistence.entity.VehicleLocationEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/tracking/locations")
public class TrackingController {

    private final VehicleLocationService service;

    public TrackingController(VehicleLocationService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<VehicleLocationEntity> save(
            @RequestBody VehicleLocationDto dto
    ) {

        VehicleLocationEntity entity =
                new VehicleLocationEntity(
                        null,
                        dto.vehicleId(),
                        dto.lat(),
                        dto.lng(),
                        Instant.parse(dto.timestamp())
                );

        return new ApiResponse<>(
                "Tracking location saved successfully.",
                service.save(entity)
        );
    }

    @GetMapping("/{vehicleId}/latest")
    public VehicleLocationEntity latest(
            @PathVariable String vehicleId
    ) {
        return service.getLatest(vehicleId);
    }

    @GetMapping("/{vehicleId}/history")
    public List<VehicleLocationEntity> history(
            @PathVariable String vehicleId
    ) {
        return service.getHistory(vehicleId);
    }
}
