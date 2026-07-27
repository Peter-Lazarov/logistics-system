package com.sunny.times.shipments.mapper;

import com.sunny.times.shipments.api.dto.CreateShipmentRequest;
import com.sunny.times.shipments.api.dto.ShipmentResponse;
import com.sunny.times.shipments.domain.model.Shipment;
import com.sunny.times.shipments.persistence.entity.ShipmentEntity;

public interface ShipmentMapper {
    Shipment toDomain(CreateShipmentRequest request);

    ShipmentEntity toEntity(Shipment domain);

    Shipment toDomain(ShipmentEntity entity);

    ShipmentResponse toResponse(Shipment domain);
}
