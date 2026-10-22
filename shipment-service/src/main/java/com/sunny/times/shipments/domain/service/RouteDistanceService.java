package com.sunny.times.shipments.domain.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class RouteDistanceService {

    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final Map<String, String> ROUTE_FILES = Map.of(
            "BOTEVGRADSKO", "botevgradsko-route.json",
            "BOJURISHTE", "bojurishte-route.json",
            "KAZICHENE", "kazichene-airport-route.json"
    );
    private final ObjectMapper objectMapper;
    private Map<String, Double> routeDistancesKm = Map.of();

    public RouteDistanceService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    void loadRouteDistances() {
        Map<String, Double> distances = new java.util.HashMap<>();
        ROUTE_FILES.forEach((route, fileName) -> distances.put(route, readDistance(fileName)));
        routeDistancesKm = Map.copyOf(distances);
    }

    public double getDistanceKm(String simulationRoute) {
        String route = normalizeRoute(simulationRoute);
        if (route == null) {
            return 0.0;
        }

        Double distance = routeDistancesKm.get(route);
        if (distance == null) {
            throw new IllegalArgumentException("Unknown simulation route: " + route);
        }
        return distance;
    }

    public String normalizeRoute(String simulationRoute) {
        if (simulationRoute == null || simulationRoute.isBlank()) {
            return null;
        }

        String normalizedRoute = simulationRoute.trim().toUpperCase();
        if (!routeDistancesKm.containsKey(normalizedRoute)) {
            throw new IllegalArgumentException("Unknown simulation route: " + simulationRoute);
        }
        return normalizedRoute;
    }

    private double readDistance(String fileName) {
        try (InputStream input = new ClassPathResource("routes/" + fileName).getInputStream()) {
            List<RoutePoint> points = objectMapper.readValue(input, new TypeReference<>() {});
            if (points.size() < 2) {
                throw new IllegalStateException("Route must contain at least two points: " + fileName);
            }

            double totalDistance = 0.0;
            for (int index = 1; index < points.size(); index++) {
                totalDistance += haversineKm(points.get(index - 1), points.get(index));
            }
            return totalDistance;
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load route distance from " + fileName, exception);
        }
    }

    private double haversineKm(RoutePoint first, RoutePoint second) {
        double firstLatitude = Math.toRadians(first.lat());
        double secondLatitude = Math.toRadians(second.lat());
        double latitudeDelta = secondLatitude - firstLatitude;
        double longitudeDelta = Math.toRadians(second.lng() - first.lng());

        double a = Math.sin(latitudeDelta / 2) * Math.sin(latitudeDelta / 2)
                + Math.cos(firstLatitude) * Math.cos(secondLatitude)
                * Math.sin(longitudeDelta / 2) * Math.sin(longitudeDelta / 2);
        return EARTH_RADIUS_KM * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private record RoutePoint(double lat, double lng) {}
}