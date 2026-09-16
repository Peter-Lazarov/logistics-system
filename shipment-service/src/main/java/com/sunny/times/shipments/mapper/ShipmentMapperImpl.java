package com.sunny.times.shipments.mapper;

import com.sunny.times.shipments.domain.model.Shipment;
import com.sunny.times.shipments.persistence.entity.ShipmentEntity;
import org.springframework.stereotype.Component;

@Component
public class ShipmentMapperImpl implements ShipmentMapper {

    @Override
    public Shipment toDomain(ShipmentEntity e) {
        if (e == null) return null;

        return new Shipment(
                e.getId(),
                e.getCategory(),
                e.getDescription(),
                e.getOrigin(),
                e.getDestination(),
                e.getClientId(),
                e.getDriverId(),
                e.getVehicleId(),
                e.getPathId(),
                e.getTotalWeight(),
                e.getTotalVolume(),
                e.getPrice(),
                e.getStatus(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    @Override
    public ShipmentEntity toEntity(Shipment d) {
        if (d == null) return null;

        return new ShipmentEntity(
                d.getId(),
                d.getCategory(),
                d.getDescription(),
                d.getOrigin(),
                d.getDestination(),
                d.getClientId(),
                d.getDriverId(),
                d.getVehicleId(),
                d.getPathId(),
                d.getTotalWeight(),
                d.getTotalVolume(),
                d.getPrice(),
                d.getStatus(),
                d.getCreatedAt(),
                d.getUpdatedAt()
        );
    }
}
