package com.hubspot.integration.app.services;

import org.springframework.stereotype.Service;

@Service
public class WebhookService {
    public void processWebhook(String payload) {
        // TODO: processar evento contact.creation
        System.out.println("Recebido webhook: " + payload);
    }
}
