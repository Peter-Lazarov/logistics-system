package com.sunny.times.common.exception;

public class DriverNotFoundException extends RuntimeException {

    public DriverNotFoundException(Long driverId) {
        super("Driver not found: " + driverId);
    }
}