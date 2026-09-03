package com.sunny.times.contracts.shipments;

import java.util.List;

public record CreateShipmentRequest(
        List<ShipmentItemDto> items
) {}
