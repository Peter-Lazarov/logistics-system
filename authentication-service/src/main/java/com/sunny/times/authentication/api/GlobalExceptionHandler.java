package com.sunny.times.authentication.api;

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
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public record ValidationErrorResponse(Instant timestamp, int status, Map<String, String> errors) {
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            String existing = errors.get(error.getField());
            if (existing == null || validationPriority(error.getDefaultMessage()) < validationPriority(existing)) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
        }
        exception.getBindingResult().getGlobalErrors().forEach(error ->
                errors.putIfAbsent("request", error.getDefaultMessage())
        );
        return ResponseEntity.badRequest()
                .body(new ValidationErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), errors));
    }

    private int validationPriority(String message) {
        if (message.endsWith("is required")) return 0;
        if (message.toLowerCase().contains("invalid") || message.contains("invalid characters")) return 1;
        return 2;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ValidationErrorResponse> handleUnreadableRequest(HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest().body(new ValidationErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                Map.of("request", "Malformed JSON request")
        ));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiMessage> handleResponseStatus(ResponseStatusException exception) {
        HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());
        String message = switch (status) {
            case UNAUTHORIZED -> "Invalid email or password.".equals(exception.getReason())
                    ? "Invalid email or password."
                    : "Invalid or expired refresh token.";
            case FORBIDDEN -> "Your account has been disabled. Contact an administrator.";
            case NOT_FOUND -> "The requested user could not be found.";
            case CONFLICT -> "A user with this email already exists.";
            case BAD_REQUEST -> "The request contains invalid information.";
            default -> "The request could not be completed.";
        };
        return ResponseEntity.status(status).body(new ApiMessage(message));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiMessage> handleUsernameNotFound(UsernameNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiMessage("Invalid email or password."));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiMessage> handleEntityNotFound(EntityNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiMessage("The requested user could not be found."));
    }

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ApiMessage> handleUnknownEndpoint(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiMessage("The requested endpoint could not be found."));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiMessage> handleAccessDenied(AccessDeniedException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiMessage("You do not have permission to perform this action."));
    }

    @ExceptionHandler({DuplicateKeyException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiMessage> handleDuplicateResource(Exception exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiMessage("A user with this email already exists."));
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ApiMessage> handleInvalidRequest(Exception exception) {
        return ResponseEntity.badRequest()
                .body(new ApiMessage("Please check the submitted information."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiMessage> handleUnexpected(Exception exception) {
        log.error("Unexpected authentication service error", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiMessage("An unexpected error occurred. Please try again later."));
    }
}
