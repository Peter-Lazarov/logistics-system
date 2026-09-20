package com.sunny.times.common.routes;

import com.sunny.times.common.exception.RouteNotFoundException;
import com.sunny.times.contracts.routes.RouteDto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RouteService {

    private final RouteRepository repository;

    public RouteService(RouteRepository repository) {
        this.repository = repository;
    }

    public RouteDto getByPathId(String pathId) {
        RouteEntity entity = repository.findById(pathId)
                .orElseThrow(() -> new RouteNotFoundException(pathId));
        return RouteMapper.toDto(entity);
    }

    public List<RouteDto> getAll() {
        return repository.findAll()
                .stream()
                .map(RouteMapper::toDto)
                .toList();
    }

}
