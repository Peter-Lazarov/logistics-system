package com.sunny.times.authentication.api.dto;

public record UserResponse(
        Long id,
        String username,
        String role
) {

}

