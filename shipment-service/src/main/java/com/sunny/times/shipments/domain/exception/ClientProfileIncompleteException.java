package com.sunny.times.shipments.domain.exception;

public class ClientProfileIncompleteException extends RuntimeException {
    public ClientProfileIncompleteException() {
        super("Company, address, and VAT number are required before you can request a shipment.");
    }
}