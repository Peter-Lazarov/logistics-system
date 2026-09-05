# Shipment Workflow — Overview

The Shipment Workflow module coordinates shipment status transitions between two microservices:

- **Tracking Service** — emits tracking events such as `CREATED`, `IN_TRANSIT`, `READY_FOR_PICKUP`, `OUT_FOR_DELIVERY`, `DELIVERED`, `FAILED_DELIVERY`.
- **Shipments Service** — stores the current shipment status.

The Workflow Service acts as the decision layer between them. It determines the next valid shipment status, applies protection rules, and updates the Shipments Service only when the transition is allowed.

---

## Lifecycle

CREATED → IN_TRANSIT → READY_FOR_PICKUP → OUT_FOR_DELIVERY → DELIVERED

↘ 
FAILED_DELIVERY → CANCELLED

---

## Decision Logic

The workflow converts tracking events into shipment statuses:

- `CREATED` → `IN_TRANSIT`
- `IN_TRANSIT` → `READY_FOR_PICKUP`
- `OUT_FOR_DELIVERY` → `DELIVERED`
- `FAILED_DELIVERY` → `CANCELLED`
- `DELIVERED` → remains `DELIVERED` (final state)

---

## Protection Logic

The workflow prevents invalid or backward transitions:

- Final states (`DELIVERED`, `CANCELLED`) are never updated.
- Status transitions never move backwards in the lifecycle.
- If the next status is lower than the current lifecycle order, the update is skipped.

---

## Processing Flow

1. Fetch the latest tracking event for the shipment.
2. Determine the next shipment status based on the event.
3. Retrieve the current shipment status from the Shipments Service.
4. Apply protection logic:
   - Skip if the transition is invalid.
   - Skip if the shipment is already in a final state.
5. If valid, update the shipment status in the Shipments Service.

---

## Components

- **WorkflowService** — core business logic.
- **WorkflowController** — REST API endpoint for triggering workflow updates.
- **TrackingClient** — Feign client for retrieving tracking events.
- **ShipmentsClient** — Feign client for updating shipment status and retrieving current state.

---

## Logging

The workflow logs:

- Current and next status
- Skip reasons
- Successful updates
- Errors from external services

---

## Recommended File Location

Place this README section inside the microservice that implements the workflow:

