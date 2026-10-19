package com.sunny.times.shipments.api;

import com.sunny.times.contracts.common.ApiMessage;
import com.sunny.times.shipments.domain.exception.ShipmentNotFoundException;
import com.sunny.times.shipments.domain.exception.ClientProfileIncompleteException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ShipmentNotFoundException.class)
    public ResponseEntity<ApiMessage> handleShipmentNotFound(ShipmentNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiMessage("Shipment not found."));
    }

    @ExceptionHandler(ClientProfileIncompleteException.class)
    public ResponseEntity<ApiMessage> handleClientProfileIncomplete(ClientProfileIncompleteException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiMessage(exception.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiMessage> handleEntityNotFound(EntityNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiMessage("The requested resource could not be found."));
    }

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ApiMessage> handleUnknownEndpoint(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiMessage("The requested endpoint could not be found."));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiMessage> handleUsernameNotFound(UsernameNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiMessage("The account could not be found. Please sign in again."));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiMessage> handleAccessDenied(AccessDeniedException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiMessage("You do not have permission to perform this action."));
    }

    @ExceptionHandler({DuplicateKeyException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiMessage> handleDuplicateResource(Exception exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiMessage("A shipment with these details already exists."));
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiMessage> handleInvalidRequest(Exception exception) {
        return ResponseEntity.badRequest()
                .body(new ApiMessage("Please check the submitted shipment details."));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiMessage> handleInvalidShipmentState(IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiMessage("The shipment cannot be changed in its current status."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiMessage> handleUnexpected(Exception exception) {
        log.error("Unexpected shipment service error", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiMessage("An unexpected error occurred. Please try again later."));
    }
}
