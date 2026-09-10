package com.sunny.times.shipments.mapper;

import com.sunny.times.shipments.domain.model.ShipmentItem;
import com.sunny.times.shipments.persistence.entity.ShipmentItemEntity;
import org.springframework.stereotype.Component;

@Component
public class ShipmentItemMapperImpl implements ShipmentItemMapper {

    @Override
    public ShipmentItem toDomain(ShipmentItemEntity e) {
        if (e == null) return null;

        return new ShipmentItem(
                e.getId(),
                e.getName(),
                e.getDescription(),
                e.getQuantity()
        );
    }

    @Override
    public ShipmentItemEntity toEntity(ShipmentItem d) {
        if (d == null) return null;

        return new ShipmentItemEntity(
                d.getId(),
                d.getName(),
                d.getDescription(),
                d.getQuantity(),
                null
        );
    }
}
