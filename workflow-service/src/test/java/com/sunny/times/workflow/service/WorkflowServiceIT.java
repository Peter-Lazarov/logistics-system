package com.sunny.times.workflow.service;

import com.sunny.times.contracts.shipments.ShipmentResponse;
import com.sunny.times.contracts.shipments.ShipmentStatusDto;
import com.sunny.times.contracts.shipments.UpdateShipmentStatusRequest;
import com.sunny.times.contracts.tracking.TrackingEventResponse;
import com.sunny.times.workflow.client.ShipmentsClient;
import com.sunny.times.workflow.client.TrackingClient;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest
class WorkflowServiceIT {

    @MockBean
    TrackingClient trackingClient;

    @MockBean
    ShipmentsClient shipmentsClient;

    @Test
    void integration_updateShipmentStatus() {
        UUID id = UUID.randomUUID();

        // Mock tracking event
        when(trackingClient.getLatest(id))
                .thenReturn(new TrackingEventResponse("IN_TRANSIT", Instant.now(), null));

        // Mock shipment state
        when(shipmentsClient.getShipment(id))
                .thenReturn(new ShipmentResponse(id, ShipmentStatusDto.IN_TRANSIT));

        // Run real Spring bean
        WorkflowService service = new WorkflowService(trackingClient, shipmentsClient);
        service.updateShipmentStatus(id);

        // Verify update
        verify(shipmentsClient)
                .updateStatus(eq(id), any(UpdateShipmentStatusRequest.class));
    }
}
