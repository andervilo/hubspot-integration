package com.hubspot.integration.infra.repositories;

import com.hubspot.integration.domain.entities.HubspotWebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubspotWebhookEventRepository extends JpaRepository<HubspotWebhookEvent, Long> {
}
