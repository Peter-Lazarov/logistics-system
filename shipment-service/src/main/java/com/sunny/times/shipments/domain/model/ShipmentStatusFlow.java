package com.sunny.times.shipments.domain.model;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class ShipmentStatusFlow {

    private static final Map<ShipmentStatus, Set<ShipmentEvent>> transitions = new EnumMap<>(ShipmentStatus.class);

    static {
        transitions.put(ShipmentStatus.CREATED,
                EnumSet.of(ShipmentEvent.ASSIGN_DRIVER, ShipmentEvent.CANCEL));

        transitions.put(ShipmentStatus.ASSIGNED,
                EnumSet.of(ShipmentEvent.LOAD_SHIPMENT, ShipmentEvent.CANCEL));

        transitions.put(ShipmentStatus.LOADED,
                EnumSet.of(ShipmentEvent.START_ROUTE, ShipmentEvent.CANCEL));

        transitions.put(ShipmentStatus.IN_TRANSIT,
                EnumSet.of(ShipmentEvent.ARRIVE_DESTINATION, ShipmentEvent.DELAY, ShipmentEvent.CANCEL));

        transitions.put(ShipmentStatus.DELAYED,
                EnumSet.of(ShipmentEvent.START_ROUTE, ShipmentEvent.CANCEL));

        transitions.put(ShipmentStatus.ARRIVED,
                EnumSet.of(ShipmentEvent.COMPLETE_DELIVERY, ShipmentEvent.CANCEL));

        transitions.put(ShipmentStatus.DELIVERED,
                EnumSet.noneOf(ShipmentEvent.class));

        transitions.put(ShipmentStatus.CANCELLED,
                EnumSet.noneOf(ShipmentEvent.class));
    }

    public static boolean isAllowed(ShipmentStatus currentStatus, ShipmentEvent event) {
        return transitions.getOrDefault(currentStatus, EnumSet.noneOf(ShipmentEvent.class))
                .contains(event);
    }
}
