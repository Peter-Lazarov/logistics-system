package com.sunny.times.workflow.api;

import com.sunny.times.workflow.service.WorkflowService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/workflow")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping("/{shipmentId}/update-status")
    public void updateStatus(@PathVariable UUID shipmentId) {
        workflowService.updateShipmentStatus(shipmentId);
    }
}
