package com.sunny.times.contracts.common;

public record ApiResponse<T>(String message, T data) {}
