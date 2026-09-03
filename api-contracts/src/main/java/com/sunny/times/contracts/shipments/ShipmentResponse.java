package com.sunny.times.contracts.shipments;

import java.util.UUID;

public record ShipmentResponse(
        UUID id,
        ShipmentStatusDto status
) {}
