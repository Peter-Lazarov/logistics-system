package com.sunny.times.common.exception;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(Long clientId) {
        super("Client not found: " + clientId);
    }
}