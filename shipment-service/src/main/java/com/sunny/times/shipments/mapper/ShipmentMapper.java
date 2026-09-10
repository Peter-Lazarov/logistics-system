package com.sunny.times.shipments.mapper;

import com.sunny.times.shipments.domain.model.Shipment;
import com.sunny.times.shipments.persistence.entity.ShipmentEntity;
import org.springframework.stereotype.Component;

@Component
public interface ShipmentMapper {

    Shipment toDomain(ShipmentEntity entity);

    ShipmentEntity toEntity(Shipment domain);
}
