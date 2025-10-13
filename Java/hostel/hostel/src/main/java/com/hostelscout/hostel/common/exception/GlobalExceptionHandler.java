package com.hostelscout.hostel.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle Resource Not Found exceptions
    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<?> handleResourceConflictException(ResourceConflictException ex) {
        return new ResponseEntity<>(
                Map.of(
                        "error", ex.getMessage(),
                        "status", HttpStatus.CONFLICT.value(),
                        "timestamp", LocalDateTime.now()
                ),
                HttpStatus.CONFLICT
        );
    }

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return new ResponseEntity<>(
                Map.of(
                        "error", ex.getMessage(),
                        "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "timestamp", LocalDateTime.now()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }



}
