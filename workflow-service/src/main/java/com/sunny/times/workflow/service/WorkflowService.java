package com.sunny.times.workflow.service;

import com.sunny.times.contracts.shipments.ShipmentResponse;
import com.sunny.times.contracts.shipments.ShipmentStatusDto;
import com.sunny.times.contracts.shipments.UpdateShipmentStatusRequest;
import com.sunny.times.workflow.client.ShipmentsClient;
import com.sunny.times.workflow.client.TrackingClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class WorkflowService {

    private final TrackingClient trackingClient;
    private final ShipmentsClient shipmentsClient;

    public WorkflowService(TrackingClient trackingClient, ShipmentsClient shipmentsClient) {
        this.trackingClient = trackingClient;
        this.shipmentsClient = shipmentsClient;
    }

    public ShipmentStatusDto decideNextStatus(String eventType) {
        return switch (eventType) {
            case "CREATED" -> ShipmentStatusDto.IN_TRANSIT;
            case "IN_TRANSIT" -> ShipmentStatusDto.READY_FOR_PICKUP;
            case "OUT_FOR_DELIVERY" -> ShipmentStatusDto.DELIVERED;
            case "DELIVERED" -> ShipmentStatusDto.DELIVERED; // final state
            case "FAILED_DELIVERY" -> ShipmentStatusDto.CANCELLED;
            default -> throw new IllegalArgumentException("Unknown event type: " + eventType);
        };
    }

    private boolean shouldSkip(ShipmentStatusDto current, ShipmentStatusDto next) {
        if (current == ShipmentStatusDto.DELIVERED) return true;
        if (current == ShipmentStatusDto.CANCELLED) return true;

        if (lifecycleOrder(next) < lifecycleOrder(current)) return true;

        return false;
    }

    private int lifecycleOrder(ShipmentStatusDto status) {
        return switch (status) {
            case CREATED -> 0;
            case IN_TRANSIT -> 1;
            case READY_FOR_PICKUP -> 2;
            case OUT_FOR_DELIVERY -> 3;
            case DELIVERED -> 4;
            case CANCELLED -> 5;
        };
    }

    public void updateShipmentStatus(UUID shipmentId) {

        // Fetch latest tracking event
        var latest = trackingClient.getLatest(shipmentId);
        if (latest == null) {
            log.warn("Workflow: no tracking event found for {}", shipmentId);
            return;
        }

        var nextStatus = decideNextStatus(latest.getStatus());

        // Fetch current shipment state
        ShipmentResponse currentShipment;
        try {
            currentShipment = shipmentsClient.getShipment(shipmentId);
        } catch (Exception e) {
            log.error("Workflow: failed to fetch shipment {}", shipmentId, e);
            return; // swallow exception → test expects this
        }

        var currentStatus = currentShipment.status();

        log.info("Workflow: shipment {} current={}, next={}", shipmentId, currentStatus, nextStatus);

        // Protection logic
        if (shouldSkip(currentStatus, nextStatus)) {
            log.debug("Workflow: skipping update for {} (current={}, next={})",
                    shipmentId, currentStatus, nextStatus);
            return;
        }

        // Apply status update
        try {
            shipmentsClient.updateStatus(
                    shipmentId,
                    new UpdateShipmentStatusRequest(nextStatus)
            );
            log.info("Workflow: updated shipment {} to {}", shipmentId, nextStatus);
        } catch (Exception e) {
            log.error("Workflow: failed to update shipment {} to {}", shipmentId, nextStatus, e);
        }
    }
}
