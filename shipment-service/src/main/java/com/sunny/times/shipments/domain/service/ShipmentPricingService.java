package com.sunny.times.shipments.domain.service;

import com.sunny.times.shipments.persistence.entity.ShipmentEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ShipmentPricingService {

    private final RouteDistanceService routeDistanceService;

    public ShipmentPricingService(RouteDistanceService routeDistanceService) {
        this.routeDistanceService = routeDistanceService;
    }

    public void calculateAndStore(ShipmentEntity shipment) {
        String route = routeDistanceService.normalizeRoute(shipment.getSimulationRoute());
        double distanceKm = route == null
            ? shipment.getDistanceKm() == null || shipment.getDistanceKm() <= 0.0
                ? 1.0
                : shipment.getDistanceKm()
            : routeDistanceService.getDistanceKm(route);

        BigDecimal calculatedPrice = new BigDecimal("15.00")
                .add(BigDecimal.valueOf(shipment.getTotalWeight()).multiply(new BigDecimal("0.005")))
                .add(BigDecimal.valueOf(shipment.getTotalVolume()))
                .add(BigDecimal.valueOf(distanceKm))
                .max(new BigDecimal("25.00"))
                .setScale(2, RoundingMode.HALF_UP);

        shipment.setSimulationRoute(route);
        shipment.setDistanceKm(distanceKm);
        shipment.setPrice(calculatedPrice.doubleValue());
    }
}