package com.sunny.times.shipments.api;

import com.sunny.times.shipments.api.error.ProblemDetails;
import com.sunny.times.shipments.domain.exception.ShipmentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetails> handleValidationErrors(MethodArgumentNotValidException exception){
        String details = exception.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .reduce("", (a, b) -> a + "; " + b);

        ProblemDetails problem = new ProblemDetails(
                "https://api.shipments/errors/validation",
                "Validation Error",
                400,
                details,
                "/shipments",
                Instant.now()
        );

        log.warn("Validation error: {}", details);

        return ResponseEntity.status(400).body(problem);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetails> handleRuntime(RuntimeException exception){

        ProblemDetails problem = new ProblemDetails(
                "https://api.shipments/errors/internal",
                "Internal Server Error",
                500,
                exception.getMessage(),
                "/shipments",
                Instant.now()
        );

        log.error("Unexpected error", exception);

        return ResponseEntity.status(500).body(problem);
    }

    @ExceptionHandler(ShipmentNotFoundException.class)
    public ResponseEntity<ProblemDetails> handleNotFound(ShipmentNotFoundException exception) {
        ProblemDetails problem = new ProblemDetails(
                "https://api.shipments/errors/not-found",
                "Shipment Not Found",
                404,
                exception.getMessage(),
                "/shipments",
                Instant.now()
        );

        log.warn("Shipment not found: {}", exception.getMessage());

        return ResponseEntity.status(404).body(problem);
    }

}
