package com.hubspot.integration.app.services;

import com.hubspot.integration.app.clients.HubSpotContactClient;
import com.hubspot.integration.app.dto.command.CreateContactCommand;
import com.hubspot.integration.app.dto.command.HubSpotContactCreateCommand;
import com.hubspot.integration.infra.configs.OAuthConfig;
import com.hubspot.integration.infra.utils.RateLimiter;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactService {
    private final OAuthConfig config;
    private final RateLimiter rateLimiter;
    private final HubTokenService tokenService;
    private final HubSpotContactClient hubSpotContactClient;

    public void createContact(CreateContactCommand command) {
        var hubspotContact = HubSpotContactCreateCommand.from(command);
        var hubspotToken = tokenService.getToken();
        var token = "Bearer "+ hubspotToken.getAccessToken();

        try{
            hubSpotContactClient.createContact(token, hubspotContact);
        } catch (FeignException e) {
            log.error("Erro ao chamar HubSpot: {} {}", + e.status(), e.getMessage());
            throw e;
        }

        //rateLimiter.acquire();
    }
}
