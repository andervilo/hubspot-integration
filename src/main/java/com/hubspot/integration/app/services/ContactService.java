package com.hubspot.integration.app.services;

import com.hubspot.integration.app.clients.HubSpotContactClient;
import com.hubspot.integration.app.dto.command.CreateContactCommand;
import com.hubspot.integration.app.dto.command.HubSpotContactCreateCommand;
import com.hubspot.integration.infra.configs.OAuthConfig;
import com.hubspot.integration.infra.exception.AuthenticateException;
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

        if (hubspotToken == null) {
            log.error("CreateContact -> Hubspot token not found");
            throw new AuthenticateException("Hubspot token not found");
        }

        if(hubspotToken.isExpired()) {
            log.error("CreateContact -> Hubspot token expired");
            throw new AuthenticateException("Hubspot token expired");
        }

        var token = "Bearer "+ hubspotToken.getAccessToken();

        try{
            hubSpotContactClient.createContact(token, hubspotContact);
        } catch (FeignException e) {
            log.error("CreateContact -> Error on call HubSpot: {} {}", + e.status(), e.getMessage());
            throw e;
        }

        //rateLimiter.acquire();
    }
}
