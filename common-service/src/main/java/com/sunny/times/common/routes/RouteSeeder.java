package com.sunny.times.common.routes;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteSeeder implements CommandLineRunner {

    private final RouteRepository routeRepository;

    public RouteSeeder(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Override
    public void run(String... args) {
        if (routeRepository.existsById("1102")) {
            return;
        }

        RouteEntity route = new RouteEntity("1102", "Sofia", "Plovdiv", "2h 30m");
        List.of(
                new double[]{42.6977, 23.3219},
                new double[]{42.5736, 23.8129},
                new double[]{42.1354, 24.7453}
        ).forEach(point -> route.getPoints().add(
                new RoutePointEntity(point[0], point[1], route)
        ));

        routeRepository.save(route);
    }
}