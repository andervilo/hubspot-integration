package com.hubspot.integration.app.dto.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HubspotWebhookEventCommand {
    private Long eventId;
    private Long subscriptionId;
    private Long portalId;
    private Long appId;
    private Long occurredAt;
    private String subscriptionType;
    private Long objectId;
    private String objectType;
    private String changeSource;
}
