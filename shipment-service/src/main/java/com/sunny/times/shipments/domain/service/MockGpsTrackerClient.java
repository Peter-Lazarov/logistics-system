package com.sunny.times.shipments.domain.service;

import com.sunny.times.contracts.tracking.StartSimulationRequest;
import com.sunny.times.contracts.tracking.StopSimulationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class MockGpsTrackerClient {

    private static final Logger logger = LoggerFactory.getLogger(MockGpsTrackerClient.class);

    private final WebClient webClient;

    public MockGpsTrackerClient(
            @Value("${mock.gps.tracker.url:http://localhost:3001}") String baseUrl
    ) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public void startSimulation(StartSimulationRequest request) {
        try {
            webClient.post()
                    .uri("/simulations/start")
                    .bodyValue(request)
                    .retrieve()
                    .toBodilessEntity()
                    .timeout(Duration.ofSeconds(2))
                    .onErrorResume(error -> {
                        logger.warn("Could not start GPS simulation for shipment {}: {}",
                            request.shipmentId(), error.getMessage());
                        return Mono.empty();
                    })
                    .block();
        } catch (RuntimeException error) {
            logger.warn("Could not start GPS simulation for shipment {}: {}",
                    request.shipmentId(), error.getMessage());
        }
    }

    public void stopSimulation(StopSimulationRequest request) {
        try {
            webClient.post()
                    .uri("/simulations/stop")
                    .bodyValue(request)
                    .retrieve()
                    .toBodilessEntity()
                    .timeout(Duration.ofSeconds(2))
                    .onErrorResume(error -> {
                        logger.warn("Could not stop GPS simulation for shipment {}: {}",
                                request.shipmentId(), error.getMessage());
                        return Mono.empty();
                    })
                    .block();
        } catch (RuntimeException error) {
            logger.warn("Could not stop GPS simulation for shipment {}: {}",
                    request.shipmentId(), error.getMessage());
        }
    }
}