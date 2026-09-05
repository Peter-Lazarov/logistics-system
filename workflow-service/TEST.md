# WorkflowService Tests

This document describes the middle‑level unit tests for the WorkflowService component.

---

## Tested Components

- `decideNextStatus(String eventType)`
- `lifecycleOrder(ShipmentStatusDto status)`
- `shouldSkip(ShipmentStatusDto current, ShipmentStatusDto next)`
- `updateShipmentStatus(UUID shipmentId)` — with Mockito mocks

---

## Test Cases

### 1. decideNextStatus

- CREATED → IN_TRANSIT
- IN_TRANSIT → READY_FOR_PICKUP
- OUT_FOR_DELIVERY → DELIVERED
- FAILED_DELIVERY → CANCELLED
- DELIVERED → DELIVERED
- Invalid event → throws IllegalArgumentException

---

### 2. lifecycleOrder

Ensures correct numeric ordering:

- CREATED = 0
- IN_TRANSIT = 1
- READY_FOR_PICKUP = 2
- OUT_FOR_DELIVERY = 3
- DELIVERED = 4
- CANCELLED = 5

---

### 3. shouldSkip

- Skip when current is DELIVERED or CANCELLED
- Skip when next < current (backward transition)
- Do NOT skip when next > current (valid forward transition)

---

### 4. updateShipmentStatus (Mockito)

- Updates status when transition is valid
- Skips update when shipment is in final state
- Skips update when no tracking event exists
- Handles Feign errors gracefully (logged, not thrown)

---

## Example Test Snippets

```java
assertEquals(ShipmentStatusDto.IN_TRANSIT, service.decideNextStatus("CREATED"));
assertThrows(IllegalArgumentException.class, () -> service.decideNextStatus("UNKNOWN"));
assertTrue(service.shouldSkip(ShipmentStatusDto.DELIVERED, ShipmentStatusDto.IN_TRANSIT));
verify(shipmentsClient).updateStatus(eq(id), any(UpdateShipmentStatusRequest.class));
verify(shipmentsClient, never()).updateStatus(any(), any());
