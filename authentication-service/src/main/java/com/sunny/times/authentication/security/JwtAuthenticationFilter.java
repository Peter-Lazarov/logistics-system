package com.sunny.times.authentication.security;

import com.sunny.times.authentication.model.UserStatus;
import com.sunny.times.authentication.model.User;
import com.sunny.times.authentication.repository.UserRepository;
import com.sunny.times.security.ApiErrorWriter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final SecretKey accessKey;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            @Value("${jwt.access-secret}") String accessSecret,
            UserRepository userRepository
    ) {
        this.accessKey = Keys.hmacShaKeyFor(
                accessSecret.getBytes(StandardCharsets.UTF_8)
        );
        this.userRepository = userRepository;
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

        Claims claims;
        List<?> tokenRoles;
        List<String> roles;
        String email;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(accessKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            email = claims.getSubject();
            tokenRoles = claims.get("roles", List.class);
            if (email == null || tokenRoles == null
                    || tokenRoles.stream().anyMatch(role -> !(role instanceof String))) {
                throw new IllegalArgumentException("Invalid access token claims");
            }
            roles = tokenRoles.stream().map(String.class::cast).toList();
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException exception) {
            filterChain.doFilter(request, response);
            return;
        }

        final User user;
        try {
            user = userRepository.findByEmail(email).orElse(null);
        } catch (DataAccessException exception) {
            log.error("Unable to verify the authenticated user's status", exception);
            ApiErrorWriter.write(
                    response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred. Please try again later."
            );
            return;
        }
        if (user != null && user.getStatus() != UserStatus.ACTIVE) {
            ApiErrorWriter.write(
                    response,
                    HttpServletResponse.SC_FORBIDDEN,
                    "Your account has been disabled. Contact an administrator."
            );
            return;
        }
        if (user == null) {
            ApiErrorWriter.write(
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid or expired access token."
            );
            return;
        }

        var authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(email, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}