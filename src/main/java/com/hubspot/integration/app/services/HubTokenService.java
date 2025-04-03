package com.hubspot.integration.app.services;

import com.hubspot.integration.app.dto.response.HubspotTokenResponse;
import com.hubspot.integration.domain.entities.HubspotToken;
import com.hubspot.integration.infra.repositories.HubspotTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubTokenService {

    public static final long ID = 1L;
    private final HubspotTokenRepository tokenRepository;

    public void saveToken(HubspotTokenResponse tokenResponse) {
        HubspotToken existingToken = tokenRepository.findById(ID).orElse(null);
        if (existingToken != null) {
            existingToken.update(
                    tokenResponse.getAccessToken(),
                    tokenResponse.getRefreshToken(),
                    tokenResponse.getTokenType(),
                    tokenResponse.getExpiresIn()
            );
            tokenRepository.save(existingToken);
            return;
        }

        HubspotToken hubspotToken = HubspotToken.of(
                tokenResponse.getAccessToken(),
                tokenResponse.getRefreshToken(),
                tokenResponse.getTokenType(),
                tokenResponse.getExpiresIn()
        );
        tokenRepository.save(hubspotToken);
    }

    public HubspotToken getToken() {
        return tokenRepository.findById(ID).orElse(null);
    }
}
