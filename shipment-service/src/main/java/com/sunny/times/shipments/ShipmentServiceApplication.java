package com.sunny.times.shipments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		scanBasePackages = {
				"com.sunny.times.shipments",
				"com.sunny.times.security"
		}
)
public class ShipmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShipmentServiceApplication.class, args);
	}
}
