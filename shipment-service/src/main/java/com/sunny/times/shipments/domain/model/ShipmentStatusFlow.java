package com.sunny.times.shipments.domain.model;

public final class ShipmentStatusFlow {

    private ShipmentStatusFlow() {
    }

    public static ShipmentStatus nextStatus(
            ShipmentStatus current,
            ShipmentEvent event) {

        return switch (current) {

            case CREATED -> switch (event) {
                case ASSIGN -> ShipmentStatus.ASSIGNED;
                case CANCEL -> ShipmentStatus.CANCELLED;
                default -> throw invalid(current, event);
            };

            case ASSIGNED -> switch (event) {
                case LOAD -> ShipmentStatus.LOADED;
                case CANCEL -> ShipmentStatus.CANCELLED;
                default -> throw invalid(current, event);
            };

            case LOADED -> switch (event) {
                case DEPART -> ShipmentStatus.IN_TRANSIT;
                case CANCEL -> ShipmentStatus.CANCELLED;
                default -> throw invalid(current, event);
            };

            case IN_TRANSIT -> switch (event) {
                case ARRIVE -> ShipmentStatus.ARRIVED;
                case DELAY -> ShipmentStatus.DELAYED;
                default -> throw invalid(current, event);
            };

            case DELAYED -> switch (event) {
                case DEPART -> ShipmentStatus.IN_TRANSIT;
                case CANCEL -> ShipmentStatus.CANCELLED;
                default -> throw invalid(current, event);
            };

            case ARRIVED -> switch (event) {
                case DELIVER -> ShipmentStatus.DELIVERED;
                default -> throw invalid(current, event);
            };

            case DELIVERED, CANCELLED ->
                    throw invalid(current, event);
        };
    }

    private static IllegalStateException invalid(
            ShipmentStatus status,
            ShipmentEvent event) {

        return new IllegalStateException(
                "Invalid transition from "
                        + status
                        + " using event "
                        + event
        );
    }
}
