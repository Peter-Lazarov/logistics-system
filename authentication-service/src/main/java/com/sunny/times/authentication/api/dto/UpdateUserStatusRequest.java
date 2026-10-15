package com.sunny.times.authentication.api.dto;

import com.sunny.times.authentication.model.UserStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateUserStatusRequest(
        @NotNull UserStatus status
) {
}
