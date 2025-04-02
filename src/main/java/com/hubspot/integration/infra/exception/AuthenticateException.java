package com.hubspot.integration.infra.exception;

public class AuthenticateException extends RuntimeException {
    public AuthenticateException(String message) {
        super(message);
    }
}
