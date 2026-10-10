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

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
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

        String email = jwtUtil.extractUsername(token);
        Long userId = jwtUtil.extractUserId(token);

        if (email != null
                && SecurityContextHolder
                .getContext()
                .getAuthentication() == null) {

            if (jwtUtil.isTokenValid(token)) {

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
