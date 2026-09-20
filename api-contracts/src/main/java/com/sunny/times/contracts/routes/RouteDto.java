package com.sunny.times.contracts.routes;

import java.util.List;

public record RouteDto(
        String pathId,
        String origin,
        String destination,
        String estimatedTime,
        List<RoutePointDto> points
) {}
