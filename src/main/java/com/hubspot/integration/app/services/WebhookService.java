package com.hubspot.integration.app.services;

import com.hubspot.integration.app.dto.command.HubspotWebhookEventCommand;
import com.hubspot.integration.domain.entities.HubspotWebhookEvent;
import com.hubspot.integration.infra.repositories.HubspotWebhookEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {
    private final HubspotWebhookEventRepository eventRepository;

    public void processWebhook(final HubspotWebhookEventCommand payload) {
        log.info("[WEBHOOK RECEIVED]: {}", payload);
        var hubspotEvent = HubspotWebhookEvent.of(payload);
        eventRepository.save(hubspotEvent);
    }
}
