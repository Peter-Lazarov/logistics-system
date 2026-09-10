package com.sunny.times.workflow.api;

import com.sunny.times.workflow.client.ShipmentsClient;
import com.sunny.times.workflow.client.TrackingClient;
import com.sunny.times.workflow.service.WorkflowService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkflowController.class)
class WorkflowControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WorkflowService workflowService;

    @MockBean
    TrackingClient trackingClient;

    @MockBean
    ShipmentsClient shipmentsClient;

    @Test
    void updateStatus_shouldCallService() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/workflow/" + id + "/update-status"))
                .andExpect(status().isOk());

        verify(workflowService).updateShipmentStatus(id);
    }
}

