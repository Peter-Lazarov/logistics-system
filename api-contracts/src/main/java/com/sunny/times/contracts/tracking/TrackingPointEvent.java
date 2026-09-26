package com.sunny.times.contracts.tracking;

import java.io.Serializable;

public record TrackingPointEvent(
        String vehicleId,
        double lat,
        double lng,
        String timestamp
) implements Serializable {
}
