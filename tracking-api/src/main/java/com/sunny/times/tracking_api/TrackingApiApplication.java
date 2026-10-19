package com.sunny.times.tracking_api;

import com.sunny.times.security.CorsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CorsConfig.class)
public class TrackingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrackingApiApplication.class, args);
	}

}
