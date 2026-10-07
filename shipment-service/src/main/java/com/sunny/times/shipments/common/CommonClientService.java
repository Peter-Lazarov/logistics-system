package com.sunny.times.shipments.common;

import com.sunny.times.contracts.clients.ClientDto;
import com.sunny.times.contracts.routes.RouteDto;
import com.sunny.times.contracts.drivers.DriverDto;
import com.sunny.times.shipments.domain.exception.ShipmentNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CommonClientService {

    private final WebClient webClient;

    public CommonClientService(WebClient commonWebClient) {
        this.webClient = commonWebClient;
    }

    public ClientDto getClient(Long clientUserId) {
        return webClient.get()
                .uri("/clients/{id}", clientUserId)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        response -> Mono.error(new ShipmentNotFoundException("Client not found: " + clientUserId)))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RuntimeException("Common Service unavailable")))
                .bodyToMono(ClientDto.class)
                .block();
    }

    public RouteDto getRoute(String pathId) {
        return webClient.get()
                .uri("/routes/{id}", pathId)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        response -> Mono.error(new ShipmentNotFoundException("Route not found: " + pathId)))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RuntimeException("Common Service unavailable")))
                .bodyToMono(RouteDto.class)
                .block();
    }

    public DriverDto getDriver(Long driverUserId) {
        return webClient.get()
                .uri("/drivers/{id}", driverUserId)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        response -> Mono.error(new ShipmentNotFoundException("Driver not found: " + driverUserId)))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RuntimeException("Common Service unavailable")))
                .bodyToMono(DriverDto.class)
                .block();
    }
}
