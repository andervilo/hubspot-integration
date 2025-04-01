package com.hubspot.integration.infra.repositories;

import com.hubspot.integration.domain.entities.HubspotToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubspotTokenRepository extends JpaRepository<HubspotToken, Long> {
}
