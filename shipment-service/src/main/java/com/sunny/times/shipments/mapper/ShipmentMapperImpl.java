package com.sunny.times.shipments.mapper;

import com.sunny.times.shipments.domain.model.Shipment;
import com.sunny.times.shipments.domain.model.ShipmentItem;
import com.sunny.times.shipments.persistence.entity.ShipmentEntity;
import com.sunny.times.shipments.persistence.entity.ShipmentItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShipmentMapperImpl implements ShipmentMapper {

    private final ShipmentItemMapper itemMapper;

    public ShipmentMapperImpl(ShipmentItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Override
    public Shipment toDomain(ShipmentEntity e) {
        if (e == null) return null;

        List<ShipmentItem> items = e.getItems() == null
                ? List.of()
                : e.getItems().stream()
                .map(itemMapper::toDomain)
                .toList();

        return new Shipment(
                e.getId(),
                e.getType(),
                e.getQuantity(),
                e.getWeight(),
                e.getVolume(),
                e.getStatus(),
                e.getOrigin(),
                e.getDestination(),
                e.getVehicleId(),
                e.getDriverId(),
                e.getPathId(),
                e.getPrice(),
                e.getCreatedAt(),
                e.getUpdatedAt(),
                items
        );
    }

    @Override
    public ShipmentEntity toEntity(Shipment d) {
        if (d == null) return null;

        ShipmentEntity entity = new ShipmentEntity(
                d.getId(),
                d.getType(),
                d.getQuantity(),
                d.getWeight(),
                d.getVolume(),
                d.getStatus(),
                d.getOrigin(),
                d.getDestination(),
                d.getVehicleId(),
                d.getDriverId(),
                d.getPathId(),
                d.getPrice(),
                d.getCreatedAt(),
                d.getUpdatedAt(),
                null
        );

        if (d.getItems() != null) {
            List<ShipmentItemEntity> itemEntities = d.getItems().stream()
                    .map(itemMapper::toEntity)
                    .peek(i -> i.setShipment(entity))
                    .toList();

            entity.setItems(itemEntities);
        }

        return entity;
    }
}
