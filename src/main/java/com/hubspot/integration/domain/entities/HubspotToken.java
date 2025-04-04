package com.hubspot.integration.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class HubspotToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Integer expiresIn;
    private LocalDateTime expiresAt;

    private HubspotToken(String accessToken, String refreshToken, String tokenType, Integer expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.expiresAt = getExpiresAt(expiresIn);
    }

    public static HubspotToken of(String accessToken, String refreshToken, String tokenType, Integer expiresIn) {
        return new HubspotToken(accessToken, refreshToken, tokenType, expiresIn);
    }

    public void update(String accessToken, String refreshToken, String tokenType, Integer expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.expiresAt = getExpiresAt(expiresIn);
    }

    private static LocalDateTime getExpiresAt(Integer expiresIn) {
        return LocalDateTime.now().plusSeconds(expiresIn);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
