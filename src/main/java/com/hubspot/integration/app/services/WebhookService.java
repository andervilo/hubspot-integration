package com.hubspot.integration.app.services;

import com.hubspot.integration.domain.entities.HubspotEvent;
import com.hubspot.integration.infra.repositories.HubspotEventRepository;
import com.hubspot.integration.infra.repositories.HubspotTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {
    private final HubspotEventRepository eventRepository;

    public void processWebhook(final String payload) {
        var hubspotEvent = HubspotEvent.of(payload);
        eventRepository.save(hubspotEvent);
        log.info("Recebido webhook: {}", payload);;
    }
}
