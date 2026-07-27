package com.sunny.times.shipments.mapper;

import com.sunny.times.shipments.api.dto.ShipmentItemDto;
import com.sunny.times.shipments.domain.model.ShipmentItem;
import com.sunny.times.shipments.persistence.entity.ShipmentItemEntity;

public interface ShipmentItemMapper {
    ShipmentItemEntity toEntity(ShipmentItem item);
    ShipmentItem toDomain(ShipmentItemEntity entity);
    ShipmentItemDto toResponse(ShipmentItem item);
}
