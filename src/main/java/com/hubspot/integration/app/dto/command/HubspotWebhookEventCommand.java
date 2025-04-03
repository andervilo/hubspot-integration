package com.hubspot.integration.app.dto.command;

import lombok.*;

@Getter
@Setter
@ToString
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
