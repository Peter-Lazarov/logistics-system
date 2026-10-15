package com.sunny.times.authentication.api.dto;

import java.time.Instant;

public record UserResponse(
        Long id,
        String email,
        String role,
        String name,
        String phone,
        Instant createdAt,
        String status
) {

}
