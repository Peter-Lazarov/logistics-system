package com.sunny.times.shipments.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RouteDistanceServiceTest {

    @Test
    void cachesDistancesFromAllConfiguredRoutes() {
        RouteDistanceService service = new RouteDistanceService(new ObjectMapper());
        service.loadRouteDistances();

        assertEquals(5.24, service.getDistanceKm("BOTEVGRADSKO"), 0.01);
        assertEquals(2.73, service.getDistanceKm("BOJURISHTE"), 0.01);
        assertEquals(8.35, service.getDistanceKm("KAZICHENE"), 0.01);
        assertEquals("BOTEVGRADSKO", service.normalizeRoute(" botevgradsko "));
        assertEquals(0.0, service.getDistanceKm(null));
    }
}