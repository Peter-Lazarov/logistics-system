package com.sunny.times.shipments.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunny.times.shipments.domain.model.ShipmentStatus;
import com.sunny.times.shipments.persistence.entity.ShipmentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ShipmentPricingServiceTest {

    private ShipmentPricingService pricingService;

    @BeforeEach
    void setUp() {
        RouteDistanceService routeDistanceService = new RouteDistanceService(new ObjectMapper());
        routeDistanceService.loadRouteDistances();
        pricingService = new ShipmentPricingService(routeDistanceService);
    }

    @Test
    void usesManuallyEnteredDistanceWhenNoPredefinedRouteIsSelected() {
        ShipmentEntity shipment = shipment(null, 7.3);

        pricingService.calculateAndStore(shipment);

        assertEquals(7.3, shipment.getDistanceKm());
        assertEquals(114.30, shipment.getPrice());
        assertNull(shipment.getSimulationRoute());
    }

    @Test
    void zeroDistanceUsesOneKilometerEstimateAndDoesNotInventRoute() {
        ShipmentEntity shipment = shipment(null, 0.0);

        pricingService.calculateAndStore(shipment);

        assertEquals(1.0, shipment.getDistanceKm());
        assertEquals(108.00, shipment.getPrice());
        assertNull(shipment.getSimulationRoute());
    }

    @Test
    void missingDistanceUsesOneKilometerEstimate() {
        ShipmentEntity shipment = shipment(null, null);

        pricingService.calculateAndStore(shipment);

        assertEquals(1.0, shipment.getDistanceKm());
        assertEquals(108.00, shipment.getPrice());
    }

    @Test
    void predefinedRouteDistanceOverridesManualDistance() {
        ShipmentEntity shipment = shipment("BOTEVGRADSKO", 99.0);

        pricingService.calculateAndStore(shipment);

        assertEquals(5.24, shipment.getDistanceKm(), 0.01);
        assertEquals(112.24, shipment.getPrice(), 0.01);
    }

    private ShipmentEntity shipment(String route, Double distanceKm) {
        return new ShipmentEntity(
                "SHP-TEST",
                "Furniture",
                "Test cargo",
                "Origin",
                "Destination",
                1L,
                2L,
                "CB1234AB",
                route,
                12000.0,
                32.0,
                distanceKm,
                0.0,
                ShipmentStatus.ASSIGNED,
                Instant.now(),
                Instant.now()
        );
    }
}