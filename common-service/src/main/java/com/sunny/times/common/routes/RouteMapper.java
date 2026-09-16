package com.sunny.times.common.routes;

import com.sunny.times.contracts.routes.RouteDto;
import com.sunny.times.contracts.routes.RoutePointDto;

import java.util.List;

public class RouteMapper {

    public static RouteDto toDto(RouteEntity entity) {
        List<RoutePointDto> points = entity.getPoints()
                .stream()
                .map(p -> new RoutePointDto(p.getLat(), p.getLon()))
                .toList();

        return new RouteDto(
                entity.getPathId(),
                entity.getStart(),
                entity.getDestination(),
                entity.getEstimatedTime(),
                points
        );
    }
}
