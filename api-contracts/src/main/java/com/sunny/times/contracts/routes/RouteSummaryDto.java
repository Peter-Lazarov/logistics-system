package com.sunny.times.contracts.routes;

public record RouteSummaryDto(
        String pathId,
        String origin,
        String destination,
        String estimatedTime
) {
}
