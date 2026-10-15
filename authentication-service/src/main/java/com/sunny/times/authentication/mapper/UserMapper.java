package com.sunny.times.authentication.mapper;

import com.sunny.times.authentication.api.dto.UserResponse;
import com.sunny.times.authentication.model.User;

public final class UserMapper {

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRoles().iterator().next().name(),
                user.getName(),
                user.getPhone(),
                user.getCreatedAt(),
                user.getStatus().name()
        );
    }
}
