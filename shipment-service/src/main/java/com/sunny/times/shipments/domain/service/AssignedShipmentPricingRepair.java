package com.sunny.times.shipments.domain.service;

import com.sunny.times.shipments.domain.model.ShipmentStatus;
import com.sunny.times.shipments.persistence.entity.ShipmentEntity;
import com.sunny.times.shipments.persistence.repository.ShipmentRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

@Component
public class AssignedShipmentPricingRepair implements ApplicationRunner {

    private final ShipmentRepository shipmentRepository;
    private final RouteDistanceService routeDistanceService;
    private final ShipmentPricingService shipmentPricingService;

    public AssignedShipmentPricingRepair(
            ShipmentRepository shipmentRepository,
            RouteDistanceService routeDistanceService,
            ShipmentPricingService shipmentPricingService
    ) {
        this.shipmentRepository = shipmentRepository;
        this.routeDistanceService = routeDistanceService;
        this.shipmentPricingService = shipmentPricingService;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        for (ShipmentEntity shipment : shipmentRepository.findAll()) {
            boolean assignedWithoutRoute = shipment.getStatus() == ShipmentStatus.ASSIGNED
                    && (shipment.getDistanceKm() == null || shipment.getDistanceKm() == 0.0)
                    && (shipment.getSimulationRoute() == null
                        || shipment.getSimulationRoute().isBlank()
                        || routeDistanceService.normalizeRoute(shipment.getSimulationRoute()) != null);

            if (assignedWithoutRoute) {
                shipmentPricingService.calculateAndStore(shipment);
                shipmentRepository.save(shipment);
            }
        }
    }
}