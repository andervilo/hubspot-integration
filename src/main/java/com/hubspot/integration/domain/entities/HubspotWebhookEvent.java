package com.hubspot.integration.domain.entities;

import com.hubspot.integration.app.dto.command.HubspotWebhookEventCommand;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jdk.jfr.Enabled;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Entity
public class HubspotWebhookEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long eventId;
    private Long subscriptionId;
    private Long portalId;
    private Long appId;
    private Long occurredAt;
    private String subscriptionType;
    private Long objectId;
    private String objectType;
    private String changeSource;

    private HubspotWebhookEvent(Long eventId, Long subscriptionId, Long portalId, Long appId, Long occurredAt,
            String subscriptionType, Long objectId, String objectType, String changeSource) {
        this.eventId = eventId;
        this.subscriptionId = subscriptionId;
        this.portalId = portalId;
        this.appId = appId;
        this.occurredAt = occurredAt;
        this.subscriptionType = subscriptionType;
        this.objectId = objectId;
        this.objectType = objectType;
        this.changeSource = changeSource;
    }

    public static HubspotWebhookEvent of(HubspotWebhookEventCommand payload) {
        return new HubspotWebhookEvent(
                payload.getEventId(),
                payload.getSubscriptionId(),
                payload.getPortalId(),
                payload.getAppId(),
                payload.getOccurredAt(),
                payload.getSubscriptionType(),
                payload.getObjectId(),
                payload.getObjectType(),
                payload.getChangeSource()
        );
    }
}
