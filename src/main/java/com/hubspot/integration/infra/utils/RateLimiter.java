package com.hubspot.integration.infra.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;

@Component
public class RateLimiter {
    private final Semaphore semaphore = new Semaphore(5); // exemplo simples

    public void acquire() {
        try {
            semaphore.acquire();
            Thread.sleep(500); // simula intervalo para respeitar rate limit
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }
}
