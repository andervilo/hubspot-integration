package com.hubspot.integration.infra.repositories;

import com.hubspot.integration.domain.entities.HubspotEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubspotEventRepository extends JpaRepository<HubspotEvent, Long> {
}
