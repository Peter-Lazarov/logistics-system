package com.sunny.times.security;
import java.util.Set;

public record UserContext(
        Long userId,
        String email,
        Set<String> roles
) {
}

