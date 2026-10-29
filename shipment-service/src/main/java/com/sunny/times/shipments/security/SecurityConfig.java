package com.sunny.times.shipments.security;

import com.sunny.times.security.JwtAuthenticationFilter;
import com.sunny.times.security.CorsConfig;
import com.sunny.times.security.JsonAccessDeniedHandler;
import com.sunny.times.security.JsonAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@Import(CorsConfig.class)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**")
                        .permitAll()

                        .requestMatchers("/shipments/admin/reset")
                        .permitAll()

                        // CLIENT
                        .requestMatchers(
                                "/shipments/requests",
                                "/shipments/my"
                        )
                        .hasRole("CLIENT")

                        // EMPLOYEE + ADMIN
                        .requestMatchers(
                                HttpMethod.GET,
                                "/shipments",
                                "/shipments/*",
                                "/shipments/*/history"
                        )
                        .hasAnyRole("EMPLOYEE", "ADMIN")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/shipments"
                        )
                        .hasAnyRole("EMPLOYEE", "ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/shipments/*"
                        )
                        .hasAnyRole("EMPLOYEE", "ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/shipments/*"
                        )
                        .hasAnyRole("EMPLOYEE", "ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/shipments/*/event"
                        )
                        .hasAnyRole("EMPLOYEE", "ADMIN")

                        .anyRequest()
                        .authenticated()
                        )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new JsonAuthenticationEntryPoint())
                        .accessDeniedHandler(new JsonAccessDeniedHandler())
                )
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
