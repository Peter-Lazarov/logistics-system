package com.sunny.times.workflow.service;

import com.sunny.times.contracts.shipments.ShipmentStatusDto;
import com.sunny.times.contracts.shipments.UpdateShipmentStatusRequest;
import com.sunny.times.workflow.client.ShipmentsClient;
import com.sunny.times.workflow.client.TrackingClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WorkflowService {

    private final TrackingClient trackingClient;
    private final ShipmentsClient shipmentsClient;

    public WorkflowService(TrackingClient trackingClient, ShipmentsClient shipmentsClient) {
        this.trackingClient = trackingClient;
        this.shipmentsClient = shipmentsClient;
    }

    public ShipmentStatusDto mapEventToStatus(String eventType) {
        return switch (eventType) {
            case "CREATED" -> ShipmentStatusDto.CREATED;
            case "IN_TRANSIT" -> ShipmentStatusDto.IN_TRANSIT;
            case "OUT_FOR_DELIVERY" -> ShipmentStatusDto.READY_FOR_PICKUP;
            case "DELIVERED" -> ShipmentStatusDto.DELIVERED;
            case "FAILED_DELIVERY" -> ShipmentStatusDto.CANCELLED;
            default -> throw new IllegalArgumentException("Unknown event type: " + eventType);
        };
    }


    public void updateShipmentStatus(UUID shipmentId) {
        var latest = trackingClient.getLatest(shipmentId);
        if (latest == null) return;

        var newStatus = mapEventToStatus(latest.getStatus());
        shipmentsClient.updateStatus(
                shipmentId,
                new UpdateShipmentStatusRequest(newStatus)
        );
    }
}
