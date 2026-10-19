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
                new DriverEntity(1003L, "BG-D-1003", "CA5678CD")
        );

        drivers.stream()
                .filter(driver -> !driverRepository.existsById(driver.getUserId()))
                .forEach(driverRepository::save);
    }
}