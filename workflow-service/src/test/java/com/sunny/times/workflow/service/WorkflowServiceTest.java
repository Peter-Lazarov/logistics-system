package com.sunny.times.workflow.service;

import com.sunny.times.contracts.tracking.TrackingEventResponse;
import com.sunny.times.contracts.shipments.ShipmentResponse;
import com.sunny.times.contracts.shipments.UpdateShipmentStatusRequest;
import com.sunny.times.contracts.shipments.ShipmentStatusDto;

import com.sunny.times.workflow.client.ShipmentsClient;
import com.sunny.times.workflow.client.TrackingClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkflowServiceTest {

    @Mock
    TrackingClient trackingClient;

    @Mock
    ShipmentsClient shipmentsClient;

    // decideNextStatus
    @Test
    void decideNextStatus_valid() {
        var s = new WorkflowService(null, null);
        assertEquals(ShipmentStatusDto.IN_TRANSIT, s.decideNextStatus("CREATED"));
        assertEquals(ShipmentStatusDto.READY_FOR_PICKUP, s.decideNextStatus("IN_TRANSIT"));
        assertEquals(ShipmentStatusDto.DELIVERED, s.decideNextStatus("OUT_FOR_DELIVERY"));
        assertEquals(ShipmentStatusDto.CANCELLED, s.decideNextStatus("FAILED_DELIVERY"));
        assertEquals(ShipmentStatusDto.DELIVERED, s.decideNextStatus("DELIVERED"));
    }

    @Test
    void decideNextStatus_invalid() {
        var s = new WorkflowService(null, null);
        assertThrows(IllegalArgumentException.class, () -> s.decideNextStatus("UNKNOWN"));
    }

    // updateShipmentStatus
    @Test
    void updateShipmentStatus_validUpdate() {
        UUID id = UUID.randomUUID();

        when(trackingClient.getLatest(id))
                .thenReturn(new TrackingEventResponse("IN_TRANSIT", Instant.now(), null));

        when(shipmentsClient.getShipment(id))
                .thenReturn(new ShipmentResponse(id, ShipmentStatusDto.IN_TRANSIT));

        var s = new WorkflowService(trackingClient, shipmentsClient);
        s.updateShipmentStatus(id);

        verify(shipmentsClient).updateStatus(eq(id), any(UpdateShipmentStatusRequest.class));
    }

    @Test
    void updateShipmentStatus_finalStateSkip() {
        UUID id = UUID.randomUUID();

        when(trackingClient.getLatest(id))
                .thenReturn(new TrackingEventResponse("OUT_FOR_DELIVERY", Instant.now(), null));

        when(shipmentsClient.getShipment(id))
                .thenReturn(new ShipmentResponse(id, ShipmentStatusDto.DELIVERED));

        var s = new WorkflowService(trackingClient, shipmentsClient);
        s.updateShipmentStatus(id);

        verify(shipmentsClient, never()).updateStatus(any(), any());
    }

    @Test
    void updateShipmentStatus_noTrackingEvent() {
        UUID id = UUID.randomUUID();

        when(trackingClient.getLatest(id)).thenReturn(null);

        var s = new WorkflowService(trackingClient, shipmentsClient);
        s.updateShipmentStatus(id);

        verify(shipmentsClient, never()).updateStatus(any(), any());
    }

    @Test
    void updateShipmentStatus_feignError() {
        UUID id = UUID.randomUUID();

        when(trackingClient.getLatest(id))
                .thenReturn(new TrackingEventResponse("IN_TRANSIT", Instant.now(), null));

        when(shipmentsClient.getShipment(id))
                .thenThrow(new RuntimeException("down"));

        var s = new WorkflowService(trackingClient, shipmentsClient);

        assertDoesNotThrow(() -> s.updateShipmentStatus(id));
        verify(shipmentsClient, never()).updateStatus(any(), any());
    }
}
