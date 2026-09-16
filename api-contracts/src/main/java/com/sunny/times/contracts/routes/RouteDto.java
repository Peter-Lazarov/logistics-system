package com.sunny.times.contracts.routes;

import java.util.List;

public record RouteDto(
        String pathId,
        String start,
        String destination,
        String estimatedTime,
        List<RoutePointDto> points
) {}
