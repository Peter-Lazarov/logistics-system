package com.sunny.times.shipments.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunny.times.contracts.shipments.CreateShipmentRequest;
import com.sunny.times.contracts.shipments.ShipmentResponse;
import com.sunny.times.contracts.shipments.ShipmentStatusDto;
import com.sunny.times.contracts.shipments.UpdateShipmentStatusRequest;
import com.sunny.times.shipments.domain.service.ShipmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ShipmentController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({TestSecurityConfig.class, TestApplication.class})
class ShipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShipmentService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createShipment_shouldReturn200() throws Exception {

        CreateShipmentRequest req = new CreateShipmentRequest(
                "Wood",
                "Oak planks",
                "Sofia",
                "Plovdiv",
                "CL-001",
                "DR-001",
                "CB1234AB",
                "1102",
                10000.0,
                12.5,
                120.50
        );

        ShipmentResponse response = new ShipmentResponse(
                "SHP-10001",
                "Wood",
                "Oak planks",
                "Sofia",
                "Plovdiv",
                "CL-001",
                "DR-001",
                "CB1234AB",
                "1102",
                10000.0,
                12.5,
                120.50,
                "CREATED",
                Instant.now().toString(),
                Instant.now().toString()
        );

        when(service.create(any())).thenReturn(response);

        mockMvc.perform(post("/shipments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("SHP-10001"))
                .andExpect(jsonPath("$.origin").value("Sofia"))
                .andExpect(jsonPath("$.destination").value("Plovdiv"));
    }

    @Test
    void getById_shouldReturnShipment() throws Exception {

        ShipmentResponse response = new ShipmentResponse(
                "SHP-10001",
                "Wood",
                "Oak planks",
                "Sofia",
                "Plovdiv",
                "CL-001",
                "DR-001",
                "CB1234AB",
                "1102",
                10000.0,
                12.5,
                120.50,
                "CREATED",
                Instant.now().toString(),
                Instant.now().toString()
        );

        when(service.getById("SHP-10001")).thenReturn(response);

        mockMvc.perform(get("/shipments/SHP-10001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("SHP-10001"))
                .andExpect(jsonPath("$.origin").value("Sofia"));
    }

    @Test
    void updateStatus_shouldReturnUpdatedShipment() throws Exception {

        UpdateShipmentStatusRequest req = new UpdateShipmentStatusRequest(ShipmentStatusDto.DELIVERED);

        ShipmentResponse updated = new ShipmentResponse(
                "SHP-10001",
                "Wood",
                "Oak planks",
                "Sofia",
                "Plovdiv",
                "CL-001",
                "DR-001",
                "CB1234AB",
                "1102",
                10000.0,
                12.5,
                120.50,
                "DELIVERED",
                Instant.now().toString(),
                Instant.now().toString()
        );

        when(service.updateStatus("SHP-10001", ShipmentStatusDto.DELIVERED)).thenReturn(updated);

        mockMvc.perform(patch("/shipments/SHP-10001/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"));
    }

    @Test
    void deleteShipment_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/shipments/SHP-10001"))
                .andExpect(status().isNoContent());
    }
}
