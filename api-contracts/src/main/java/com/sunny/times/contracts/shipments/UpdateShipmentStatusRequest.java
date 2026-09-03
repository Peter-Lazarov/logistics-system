package com.sunny.times.contracts.shipments;

import java.util.UUID;

public record UpdateShipmentStatusRequest(
        ShipmentStatusDto status
) {}
