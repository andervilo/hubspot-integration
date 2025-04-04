package com.hubspot.integration.rest;

import com.hubspot.integration.app.dto.command.HubspotWebhookEventCommand;
import com.hubspot.integration.app.services.HubspotSignatureService;
import com.hubspot.integration.app.services.WebhookService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {
    private final WebhookService webhookService;
    private final HubspotSignatureService signatureService;

    @PostMapping("/contact")
    public ResponseEntity<?> handleWebhook(@RequestBody List<HubspotWebhookEventCommand> command, HttpServletRequest request) throws IOException {
        if (!signatureService.isValidSignature(request)) return ResponseEntity.status(401).body("Invalid signature");
        webhookService.processWebhook(command);
        return ResponseEntity.ok().build();
    }
}
