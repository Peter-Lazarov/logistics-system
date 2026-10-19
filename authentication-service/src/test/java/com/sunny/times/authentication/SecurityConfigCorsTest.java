package com.sunny.times.authentication;

import com.sunny.times.security.CorsConfig;
import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SecurityConfigCorsTest {

    @Test
    void allowsConfiguredFrontendOrigins() {
    CorsConfiguration configuration = new CorsConfig(
                "http://localhost:4200, https://frontend.example.com"
    ).corsConfiguration();

        assertEquals(
                "https://frontend.example.com",
        configuration.checkOrigin("https://frontend.example.com")
        );
    }
}
