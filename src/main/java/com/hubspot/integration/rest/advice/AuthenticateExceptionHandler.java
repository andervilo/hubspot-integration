package com.hubspot.integration.rest.advice;

import com.hubspot.integration.infra.exception.AuthenticateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class AuthenticateExceptionHandler {

     @ExceptionHandler(AuthenticateException.class)
     public ResponseEntity<Map<String, Object>> handleAuthenticateException(AuthenticateException ex) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                 .body(Map.of(
                         "status", HttpStatus.UNAUTHORIZED.value(),
                         "category", HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                         "message", "Error while calling external service",
                         "externalMessage", ex.getMessage()
                 ));
     }
}
