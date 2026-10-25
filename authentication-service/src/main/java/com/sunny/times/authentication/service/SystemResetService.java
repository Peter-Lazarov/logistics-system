package com.sunny.times.authentication.service;

import com.sunny.times.authentication.model.Role;
import com.sunny.times.authentication.model.User;
import com.sunny.times.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class SystemResetService {

    private final UserRepository repo;
    private final WebClient webClient;

    private final String shipmentServiceUrl;
    private final String trackingServiceUrl;

    public SystemResetService(
            UserRepository repo,
            WebClient webClient,
            @Value("${shipment-service.url:http://localhost:8082}")
            String shipmentServiceUrl,
            @Value("${tracking-service.url:http://localhost:8084}")
            String trackingServiceUrl
    ) {
        this.repo = repo;
        this.webClient = webClient;
        this.shipmentServiceUrl = shipmentServiceUrl;
        this.trackingServiceUrl = trackingServiceUrl;
    }

    public void resetDemoEnvironment() {

        //Shipment Service
        webClient.post()
                .uri(shipmentServiceUrl + "/shipments/admin/reset")
                .retrieve()
                .toBodilessEntity()
                .block();

        //Tracking Servicе
        webClient.post()
                .uri(trackingServiceUrl + "/tracking/admin/reset")
                .retrieve()
                .toBodilessEntity()
                .block();

        //Authentication Service
        List<User> users = repo.findAll();

        users.stream()
                .filter(user ->
                        user.getRoles().contains(Role.ROLE_CLIENT)
                                || user.getRoles().contains(Role.ROLE_EMPLOYEE)
                )
                .forEach(repo::delete);
    }

}