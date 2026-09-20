package com.sunny.times.common.exception;

public class RouteNotFoundException extends RuntimeException {

    public RouteNotFoundException(String pathId) {
        super("Route not found: " + pathId);
    }
}