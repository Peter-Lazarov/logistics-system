package com.sunny.times.shipments.api.error;

import java.time.Instant;

public record ProblemDetails(
        String type,
        String title,
        int status,
        String detail,
        String instance,
        Instant timestamp
) {}
