package com.sunny.times.shipments.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${common.service.url}")
    private String commonServiceUrl;

    @Bean
    public WebClient commonWebClient() {

        return WebClient.builder()
                .baseUrl(commonServiceUrl)
                .build();

    }

}