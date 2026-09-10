package com.sunny.times.shipments.mapper;

import com.sunny.times.shipments.domain.model.ShipmentItem;
import com.sunny.times.shipments.persistence.entity.ShipmentItemEntity;
import org.springframework.stereotype.Component;

@Component
public interface ShipmentItemMapper {

    ShipmentItem toDomain(ShipmentItemEntity entity);

    ShipmentItemEntity toEntity(ShipmentItem domain);
}
