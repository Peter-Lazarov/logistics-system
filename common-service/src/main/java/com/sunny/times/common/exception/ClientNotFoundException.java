package com.sunny.times.common.exception;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(String clientId) {
        super("Client not found: " + clientId);
    }
}