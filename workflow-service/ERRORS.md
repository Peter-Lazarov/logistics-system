# Workflow Error Handling

## Tracking Errors

### 1. No tracking event found
- **Cause:** Tracking service returns null
- **Workflow behavior:** Skip update
- **Log:** `Workflow: no tracking event found for {id}`
- **HTTP:** 404 (if exposed through API)

### 2. Invalid event type
- **Cause:** Tracking returns unknown status
- **Workflow behavior:** `IllegalArgumentException`
- **HTTP:** 400
- **Fix:** Ensure event types match enum mapping

---

## Shipment Errors

### 3. Shipment not found
- **Cause:** Shipments service returns 404
- **Workflow behavior:** Feign exception
- **HTTP:** 404
- **Fix:** Validate shipment existence before workflow call

### 4. Status update failure
- **Cause:** Shipments service rejects update
- **Workflow behavior:** Logged error
- **HTTP:** 500
- **Fix:** Check status mapping and protection logic

---

## Feign Errors

### 5. Connection failure
- **Cause:** Service unavailable
- **Workflow behavior:** Logged error
- **HTTP:** 500
- **Fix:** Retry policy (future enhancement)

### 6. Timeout
- **Cause:** Slow downstream service
- **Workflow behavior:** Logged error
- **HTTP:** 504
- **Fix:** Increase timeout or add retry

---

## Protection Logic Errors

### 7. Backward transition
- **Cause:** next < current
- **Workflow behavior:** Skip update
- **Log:** `Workflow: skipping update for {id}`

### 8. Final state
- **Cause:** current = DELIVERED or CANCELLED
- **Workflow behavior:** Skip update
- **Log:** `Workflow: skipping update for {id}`
