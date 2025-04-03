package com.hubspot.integration.rest;

import com.hubspot.integration.app.dto.command.HubspotWebhookEventCommand;
import com.hubspot.integration.app.services.WebhookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/webhook")
public class WebhookController {
    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/contact")
    public ResponseEntity<Void> handleWebhook(@RequestBody List<HubspotWebhookEventCommand> command) {
        webhookService.processWebhook(command.get(0));
        return ResponseEntity.ok().build();
    }
}
