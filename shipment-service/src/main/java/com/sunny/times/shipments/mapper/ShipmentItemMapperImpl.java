package com.sunny.times.movement.shipments.mapper;

import com.sunny.times.movement.shipments.domain.model.ShipmentItem;
import com.sunny.times.movement.shipments.persistence.entity.ShipmentItemEntity;
import org.springframework.stereotype.Component;

@Component
public class ShipmentItemMapperImpl implements ShipmentItemMapper {

    @Override
    public ShipmentItemEntity toEntity(ShipmentItem item) {
        return new ShipmentItemEntity(
                item.getId(),
                item.getSku(),
                item.getQuantity(),
                item.getWeight()
        );
    }

    @Override
    public ShipmentItem toDomain(ShipmentItemEntity entity) {
        return new ShipmentItem(
                entity.getId(),
                entity.getSku(),
                entity.getQuantity(),
                entity.getWeight()
        );
    }
}
