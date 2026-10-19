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
        final boolean tokenValid;
        try {
            email = jwtUtil.extractUsername(token);
            userId = jwtUtil.extractUserId(token);
            tokenValid = jwtUtil.isTokenValid(token);
        } catch (RuntimeException exception) {
            filterChain.doFilter(request, response);
            return;
        }

        if (email != null
                && SecurityContextHolder
                .getContext()
                .getAuthentication() == null) {

            if (tokenValid) {
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
                    ApiErrorWriter.write(
                            response,
                            HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                            "We could not verify your account status. Please try again."
                    );
                    return;
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    ApiErrorWriter.write(
                            response,
                            HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                            "We could not verify your account status. Please try again."
                    );
                    return;
                }

                if (validationStatus != HttpServletResponse.SC_NO_CONTENT) {
                    if (validationStatus == HttpServletResponse.SC_UNAUTHORIZED
                            || validationStatus == HttpServletResponse.SC_FORBIDDEN) {
                        int status = validationStatus == HttpServletResponse.SC_FORBIDDEN
                                ? HttpServletResponse.SC_FORBIDDEN
                                : HttpServletResponse.SC_UNAUTHORIZED;
                        String message = status == HttpServletResponse.SC_FORBIDDEN
                                ? "Your account has been disabled. Contact an administrator."
                                : "Your session is no longer valid. Please sign in again.";
                        ApiErrorWriter.write(response, status, message);
                    } else {
                        ApiErrorWriter.write(
                                response,
                                HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                                "We could not verify your account status. Please try again."
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
