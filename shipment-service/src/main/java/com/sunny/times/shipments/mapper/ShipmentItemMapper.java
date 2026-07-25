package com.sunny.times.movement.shipments.mapper;

import com.sunny.times.movement.shipments.domain.model.ShipmentItem;
import com.sunny.times.movement.shipments.persistence.entity.ShipmentItemEntity;

public interface ShipmentItemMapper {
    ShipmentItemEntity toEntity(ShipmentItem item);
    ShipmentItem toDomain(ShipmentItemEntity entity);
}

