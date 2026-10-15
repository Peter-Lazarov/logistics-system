package com.sunny.times.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final URI tokenValidationUri;
    private final HttpClient httpClient;

    public JwtAuthenticationFilter(
            JwtUtil jwtUtil,
            @Value("${security.user-status-validation-url:${AUTHENTICATION_TOKEN_VALIDATION_URL:http://localhost:8080/auth/token/validate}}")
            String tokenValidationUrl
    ) {
        this.jwtUtil = jwtUtil;
        this.tokenValidationUri = URI.create(tokenValidationUrl);
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        final String email;
        final Long userId;
        try {
            email = jwtUtil.extractUsername(token);
            userId = jwtUtil.extractUserId(token);
        } catch (RuntimeException exception) {
            filterChain.doFilter(request, response);
            return;
        }

        if (email != null
                && SecurityContextHolder
                .getContext()
                .getAuthentication() == null) {

            if (jwtUtil.isTokenValid(token)) {
                final int validationStatus;
                try {
                    HttpRequest validationRequest = HttpRequest.newBuilder(tokenValidationUri)
                            .timeout(Duration.ofSeconds(3))
                            .header("Authorization", "Bearer " + token)
                            .GET()
                            .build();
                    validationStatus = httpClient.send(
                            validationRequest,
                            HttpResponse.BodyHandlers.discarding()
                    ).statusCode();
                } catch (IOException exception) {
                    response.sendError(
                            HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                            "Unable to verify account status"
                    );
                    return;
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    response.sendError(
                            HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                            "Account status verification was interrupted"
                    );
                    return;
                }

                if (validationStatus != HttpServletResponse.SC_NO_CONTENT) {
                    if (validationStatus == HttpServletResponse.SC_UNAUTHORIZED
                            || validationStatus == HttpServletResponse.SC_FORBIDDEN) {
                        response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED,
                                "User account is disabled or unavailable"
                        );
                    } else {
                        response.sendError(
                                HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                                "Unable to verify account status"
                        );
                    }
                    return;
                }

                var authorities =
                        jwtUtil.getAuthorities(token);

                UserContext userContext =
                        new UserContext(
                                userId,
                                email,
                                authorities.stream()
                                        .map(a -> a.getAuthority())
                                        .collect(
                                                java.util.stream.Collectors.toSet()
                                        )
                        );

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userContext,
                                null,
                                authorities
                        );

                auth.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }


}
