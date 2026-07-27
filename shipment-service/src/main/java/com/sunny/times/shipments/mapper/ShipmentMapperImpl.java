package com.sunny.times.shipments.mapper;

import com.sunny.times.shipments.api.dto.CreateShipmentRequest;
import com.sunny.times.shipments.api.dto.ShipmentItemDto;
import com.sunny.times.shipments.api.dto.ShipmentResponse;
import com.sunny.times.shipments.api.dto.ShipmentStatusDto;
import com.sunny.times.shipments.domain.model.Shipment;
import com.sunny.times.shipments.domain.model.ShipmentItem;
import com.sunny.times.shipments.domain.model.ShipmentStatus;
import com.sunny.times.shipments.persistence.entity.ShipmentEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class   ShipmentMapperImpl implements ShipmentMapper {

    private final ShipmentItemMapper itemMapper;

    public ShipmentMapperImpl(ShipmentItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Override
    public Shipment toDomain(CreateShipmentRequest request) {
        List<ShipmentItem> items = request.getItems().stream()
                .map(i -> new ShipmentItem(
                        null,
                        i.getSku(),
                        i.getQuantity(),
                        i.getWeight()
                ))
                .toList();

        return new Shipment(
                null,
                request.getOrderId(),
                mapStatus(request.getStatus()),
                request.getOriginWarehouseId(),
                request.getDestinationWarehouseId(),
                request.getRouteId(),
                request.getCreatedAt(),
                request.getUpdatedAt(),
                items
        );
    }

    @Override
    public ShipmentEntity toEntity(Shipment domain) {
        return new ShipmentEntity(
                domain.getId(),
                domain.getOrderId(),
                domain.getStatus(),
                domain.getOriginWarehouseId(),
                domain.getDestinationWarehouseId(),
                domain.getRouteId(),
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                domain.getItems().stream()
                        .map(itemMapper::toEntity)
                        .toList()
        );
    }

    @Override
    public Shipment toDomain(ShipmentEntity entity) {
        List<ShipmentItem> items = entity.getItems().stream()
                .map(itemMapper::toDomain)
                .toList();

        return new Shipment(
                entity.getId(),
                entity.getOrderId(),
                entity.getStatus(),
                entity.getOriginWarehouseId(),
                entity.getDestinationWarehouseId(),
                entity.getRouteId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                items
        );
    }

    @Override
    public ShipmentResponse toResponse(Shipment domain) {
        List<ShipmentItemDto> items = domain.getItems().stream()
                .map(itemMapper::toResponse)
                .toList();

        return new ShipmentResponse(
                domain.getId(),
                domain.getOrderId(),
                mapStatusDto(domain.getStatus()),
                domain.getOriginWarehouseId(),
                domain.getDestinationWarehouseId(),
                domain.getRouteId(),
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                items
        );
    }

    private ShipmentStatus mapStatus(ShipmentStatusDto dto) {
        return ShipmentStatus.valueOf(dto.name());
    }

    private ShipmentStatusDto mapStatusDto(ShipmentStatus status) {
        return ShipmentStatusDto.valueOf(status.name());
    }
}
