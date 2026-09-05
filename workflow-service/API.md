# Workflow API Documentation

## POST /workflow/{shipmentId}/update-status

Triggers the workflow to update the shipment status based on the latest tracking event.

### Request

**Method:** POST  
**Path:** `/workflow/{shipmentId}/update-status`  
**Path Variable:**
- `shipmentId` — UUID of the shipment

### Response

**200 OK**  
Status updated successfully (or skipped due to protection logic).

**404 Not Found**  
Shipment not found (from Shipments Service).  
Tracking event not found (from Tracking Service).

**400 Bad Request**  
Invalid event type returned by Tracking Service.

**500 Internal Server Error**  
Feign client failure or unexpected exception.

### Example

POST /workflow/3f1c2b2e-9a4f-4a1a-9c1e-2b3d4e5f6a7b/update-status


### Description

This endpoint:
1. Fetches latest tracking event
2. Decides next status
3. Fetches current shipment
4. Applies protection logic
5. Updates shipment status if valid  
