package com.sunny.times.contracts.tracking;

import java.io.Serializable;

public record TrackingPointEvent(
        String vehicleId,
        double latitude,
        double longitude,
        String timestamp
) implements Serializable {
}
