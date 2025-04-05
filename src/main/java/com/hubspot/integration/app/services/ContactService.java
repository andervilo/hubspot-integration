package com.hubspot.integration.app.services;

import com.hubspot.integration.app.clients.HubSpotContactClient;
import com.hubspot.integration.app.dto.command.CreateContactCommand;
import com.hubspot.integration.app.dto.command.HubSpotContactCreateCommand;
import com.hubspot.integration.infra.exception.AuthenticateException;
import io.github.resilience4j.core.functions.CheckedRunnable;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactService {

    private final HubTokenService tokenService;
    private final HubSpotContactClient hubSpotContactClient;
    private final RateLimiterRegistry rateLimiterRegistry;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public void createContact(CreateContactCommand command) {
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("hubspotRateLimiter");
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("hubspotCircuitBreaker");

        log.info("CreateContact -> Start creating contact: {}", command);
        var hubspotContact = HubSpotContactCreateCommand.from(command);
        var hubspotToken = tokenService.getToken();

        if (hubspotToken == null) {
            log.error("CreateContact -> Hubspot token not found");
            throw new AuthenticateException("Hubspot token not found");
        }

        if(hubspotToken.isExpired()) {
            log.error("CreateContact -> Hubspot token expired");
            throw new AuthenticateException("Hubspot token expired");
        }

        var token = "Bearer "+ hubspotToken.getAccessToken();

        CheckedRunnable protectedCall = RateLimiter
            .decorateCheckedRunnable(rateLimiter,
                CircuitBreaker.decorateCheckedRunnable(circuitBreaker,() -> hubSpotContactClient.createContact(token, hubspotContact))
            );

        try {
            protectedCall.run();
        } catch (Throwable t) {
            log.error("CreateContact -> Error on create Contact HubSpot: {}", t.getMessage(), t);
            fallbackCreateContact(token, hubspotContact, t);
        }
    }

    private void fallbackCreateContact(String authToken, HubSpotContactCreateCommand request, Throwable t) {
        log.warn("CreateContact -> Fallback: Contact not send to HubSpot: {}", request);
    }
}
