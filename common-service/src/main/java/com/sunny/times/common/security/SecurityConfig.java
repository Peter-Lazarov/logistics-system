package com.sunny.times.common.security;

import com.sunny.times.security.CorsConfig;
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
import com.sunny.times.security.UserContext;
import com.sunny.times.security.CurrentUserService;
import com.sunny.times.security.JwtUtil;

import com.sunny.times.security.JwtAuthenticationFilter;
import com.sunny.times.security.JsonAccessDeniedHandler;
import com.sunny.times.security.JsonAuthenticationEntryPoint;

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
                        // internal endpoint used by authentication-service
                        .requestMatchers(
                                HttpMethod.POST,
                                "/clients"
                        )
                        .permitAll()

                        .requestMatchers("/clients/me")
                        .hasAnyRole(
                                "CLIENT",
                                "EMPLOYEE",
                                "ADMIN"
                        )

                        .requestMatchers(
                                HttpMethod.GET,
                                "/clients/**"
                        )
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/drivers/**"
                        )
                        .permitAll()
                        .anyRequest().authenticated()
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
