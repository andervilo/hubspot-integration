package com.hubspot.integration.rest.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class FeignExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeignException(FeignException ex) {
        int status = ex.status();
        String responseBody = ex.contentUTF8();

        String externalMessage = "Unknown error";
        String category = null;

        try {
            Map<String, Object> bodyMap = objectMapper.readValue(responseBody, Map.class);
            externalMessage = (String) bodyMap.getOrDefault("message", "Unknown error");
            category = (String) bodyMap.getOrDefault("category", null);
        } catch (Exception e) {
            // se der erro no parse, mantém a mensagem default
        }

        return ResponseEntity.status(status)
                .body(Map.of(
                        "status", status,
                        "category", category,
                        "message", "Error while calling external service",
                        "externalMessage", externalMessage
                ));
    }
}
