package com.hubspot.integration.app.clients;

import com.hubspot.integration.app.dto.command.HubSpotContactCreateCommand;
import com.hubspot.integration.infra.configs.HubspotFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "hubspotClient", url = "${hubspot.api-url}", configuration = HubspotFeignConfig.class)
public interface HubSpotContactClient {

    @PostMapping(value = "/crm/v3/objects/contacts", consumes = MediaType.APPLICATION_JSON_VALUE)
    void createContact(@RequestHeader("Authorization") String authHeader,
                       @RequestBody HubSpotContactCreateCommand request);
}
