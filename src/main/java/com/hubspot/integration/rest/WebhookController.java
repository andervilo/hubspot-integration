package com.hubspot.integration.rest;

import com.hubspot.integration.app.dto.command.HubspotWebhookEventCommand;
import com.hubspot.integration.app.services.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {
    private final WebhookService webhookService;

    @PostMapping("/contact")
    public ResponseEntity<?> handleWebhook(@RequestBody List<HubspotWebhookEventCommand> command){
        webhookService.processWebhook(command);
        return ResponseEntity.ok().build();
    }
}
