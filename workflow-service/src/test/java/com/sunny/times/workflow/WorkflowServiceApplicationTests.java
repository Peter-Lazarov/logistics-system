package com.sunny.times.workflow;

import com.sunny.times.workflow.client.ShipmentsClient;
import com.sunny.times.workflow.client.TrackingClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class WorkflowServiceApplicationTests {

	@MockBean
	TrackingClient trackingClient;

	@MockBean
	ShipmentsClient shipmentsClient;

	@Test
	void contextLoads() {
	}
}
