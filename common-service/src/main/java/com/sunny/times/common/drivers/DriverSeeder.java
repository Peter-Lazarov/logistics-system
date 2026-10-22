package com.sunny.times.common.drivers;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DriverSeeder implements CommandLineRunner {

    private final DriverRepository driverRepository;

    public DriverSeeder(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public void run(String... args) {
        List<DriverEntity> drivers = List.of(
                new DriverEntity(1002L, "BG-D-1002", "CB1234AB"),
                new DriverEntity(1003L, "BG-D-1003", "CB5678CD"),
                new DriverEntity(1004L, "BG-D-1004", "CB9999EF")
        );

        drivers.forEach(driver -> driverRepository.findById(driver.getUserId())
            .ifPresentOrElse(existing -> {
                existing.setAssignedVehicleId(driver.getAssignedVehicleId());
                driverRepository.save(existing);
            }, () -> driverRepository.save(driver)));
    }
}