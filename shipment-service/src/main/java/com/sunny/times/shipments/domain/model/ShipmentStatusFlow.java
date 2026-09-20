package com.sunny.times.shipments.domain.model;

public final class ShipmentStatusFlow {

    private ShipmentStatusFlow() {
    }

    public static ShipmentStatus nextStatus(
            ShipmentStatus current,
            ShipmentEvent event) {

        return switch (current) {

            case CREATED -> switch (event) {
                case ASSIGN_DRIVER -> ShipmentStatus.ASSIGNED;
                case CANCEL -> ShipmentStatus.CANCELLED;
                default -> throw invalid(current, event);
            };

            case ASSIGNED -> switch (event) {
                case LOAD_SHIPMENT -> ShipmentStatus.LOADED;
                case CANCEL -> ShipmentStatus.CANCELLED;
                default -> throw invalid(current, event);
            };

            case LOADED -> switch (event) {
                case START_ROUTE -> ShipmentStatus.IN_TRANSIT;
                case CANCEL -> ShipmentStatus.CANCELLED;
                default -> throw invalid(current, event);
            };

            case IN_TRANSIT -> switch (event) {
                case ARRIVE_DESTINATION -> ShipmentStatus.ARRIVED;
                case DELAY -> ShipmentStatus.DELAYED;
                default -> throw invalid(current, event);
            };

            case DELAYED -> switch (event) {
                case START_ROUTE -> ShipmentStatus.IN_TRANSIT;
                case CANCEL -> ShipmentStatus.CANCELLED;
                default -> throw invalid(current, event);
            };

            case ARRIVED -> switch (event) {
                case COMPLETE_DELIVERY -> ShipmentStatus.DELIVERED;
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
