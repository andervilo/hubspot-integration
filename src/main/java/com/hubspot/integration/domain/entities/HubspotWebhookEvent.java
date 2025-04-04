package com.hubspot.integration.domain.entities;

import com.hubspot.integration.app.dto.command.HubspotWebhookEventCommand;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jdk.jfr.Enabled;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
    private LocalDateTime occurredAt;
    private String subscriptionType;
    private Integer attemptNumber;
    private Long objectId;
    private String changeFlag;
    private String changeSource;

    private HubspotWebhookEvent(Long eventId, Long subscriptionId, Long portalId, Long appId, Long occurredAt,
            String subscriptionType, Integer attemptNumber, Long objectId, String changeFlag, String changeSource) {
        this.eventId = eventId;
        this.subscriptionId = subscriptionId;
        this.portalId = portalId;
        this.appId = appId;
        this.occurredAt = convertLongToLocalDateTime(occurredAt);
        this.subscriptionType = subscriptionType;
        this.objectId = objectId;
        this.changeSource = changeSource;
        this.changeFlag = changeFlag;
        this.attemptNumber = attemptNumber;
    }

    public static HubspotWebhookEvent of(HubspotWebhookEventCommand payload) {
        return new HubspotWebhookEvent(
                payload.getEventId(),
                payload.getSubscriptionId(),
                payload.getPortalId(),
                payload.getAppId(),
                payload.getOccurredAt(),
                payload.getSubscriptionType(),
                payload.getAttemptNumber(),
                payload.getObjectId(),
                payload.getChangeFlag(),
                payload.getChangeSource()
        );
    }

    public static LocalDateTime convertLongToLocalDateTime(Long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }
}
