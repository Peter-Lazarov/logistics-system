package com.sunny.times.common.exception;

import com.sunny.times.contracts.common.ApiMessage;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ApiMessage> handleClientNotFound(ClientNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiMessage("Client not found."));
    }

    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<ApiMessage> handleDriverNotFound(DriverNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiMessage("Driver not found."));
    }

    @ExceptionHandler(RouteNotFoundException.class)
    public ResponseEntity<ApiMessage> handleRouteNotFound(RouteNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiMessage("Route not found."));
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
                .body(new ApiMessage("A resource with these details already exists."));
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ApiMessage> handleInvalidRequest(Exception exception) {
        return ResponseEntity.badRequest()
                .body(new ApiMessage("Please check the submitted information."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiMessage> handleUnexpected(Exception exception) {
        log.error("Unexpected common service error", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiMessage("An unexpected error occurred. Please try again later."));
    }
}