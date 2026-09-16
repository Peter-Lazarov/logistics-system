package com.sunny.times.common.routes;

import com.sunny.times.contracts.routes.RouteDto;
import org.springframework.stereotype.Service;

@Service
public class RouteService {

    private final RouteRepository repository;

    public RouteService(RouteRepository repository) {
        this.repository = repository;
    }

    public RouteDto getByPathId(String pathId) {
        RouteEntity entity = repository.findById(pathId)
                .orElseThrow(() -> new RuntimeException("Route not found: " + pathId));
        return RouteMapper.toDto(entity);
    }
}
