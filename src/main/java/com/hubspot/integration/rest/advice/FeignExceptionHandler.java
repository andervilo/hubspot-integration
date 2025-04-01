package com.hubspot.integration.rest.advice;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class FeignExceptionHandler {

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Map<String, String>> handleNotFound(FeignException.NotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "message", "Resource not found",
                        "error", ex.getMessage()
                ));
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(FeignException.BadRequest ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "message", "Invalid request",
                        "error", ex.getMessage()
                ));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, String>> handleGenericFeignException(FeignException ex) {
        return ResponseEntity.status(ex.status() > 0 ? ex.status() : 500)
                .body(Map.of(
                        "message", "Error while calling external service",
                        "error", ex.getMessage()
                ));
    }
}
