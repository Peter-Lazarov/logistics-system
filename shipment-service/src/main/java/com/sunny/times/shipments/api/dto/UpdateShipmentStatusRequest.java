package com.sunny.times.shipments.api.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateShipmentStatusRequest(
        @NotNull ShipmentStatusDto status
) {}
