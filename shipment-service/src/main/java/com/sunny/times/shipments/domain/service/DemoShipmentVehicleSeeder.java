package com.sunny.times.shipments.domain.service;

import com.sunny.times.shipments.persistence.entity.ShipmentEntity;
import com.sunny.times.shipments.persistence.repository.ShipmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DemoShipmentVehicleSeeder implements CommandLineRunner {

    private static final Map<String, String> DEMO_SHIPMENT_VEHICLES = Map.of(
            "SHP-10001", "CB1234AB",
            "SHP-10002", "CB5678CD",
            "SHP-10003", "CB9999EF"
    );

    private static final Map<Long, String> DEMO_DRIVER_VEHICLES = Map.of(
            1002L, "CB1234AB",
            1003L, "CB5678CD",
            1004L, "CB9999EF"
    );

    private final ShipmentRepository shipmentRepository;

    public DemoShipmentVehicleSeeder(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @Override
    public void run(String... args) {
        for (ShipmentEntity shipment : shipmentRepository.findAll()) {
            if (shipment.getVehicleId() != null && !shipment.getVehicleId().isBlank()) {
                continue;
            }

            String vehicleId = DEMO_SHIPMENT_VEHICLES.get(shipment.getId());
            if (vehicleId == null && shipment.getDriverUserId() != null) {
                vehicleId = DEMO_DRIVER_VEHICLES.get(shipment.getDriverUserId());
            }

            if (vehicleId != null) {
                shipment.setVehicleId(vehicleId);
                shipmentRepository.save(shipment);
            }
        }
    }
}