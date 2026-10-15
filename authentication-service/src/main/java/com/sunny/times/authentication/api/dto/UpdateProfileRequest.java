package com.sunny.times.authentication.api.dto;

public record UpdateProfileRequest(
        String name,
        String phone
) {
}
