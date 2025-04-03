package com.hubspot.integration.app.services;

import com.hubspot.integration.app.dto.response.HubspotTokenResponse;
import com.hubspot.integration.infra.configs.OAuthConfig;
import com.hubspot.integration.infra.exception.AuthenticateException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static com.hubspot.integration.app.constants.AppConstants.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OAuthConfig config;
    private final HubTokenService tokenService;
    public String generateAuthorizationUrl() {
        return UriComponentsBuilder
                .fromUriString(config.authUrl)
                .queryParam(CLIENT_ID, config.clientId)
                .queryParam(REDIRECT_URI, config.redirectUri)
                .queryParam(SCOPE, config.scopes)
                .queryParam(RESPONSE_TYPE, CODE)
                .build()
                .toUriString();
    }

    public String exchangeCodeForToken(String code) {

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add(GRANT_TYPE, AUTHORIZATION_CODE);
            body.add(CLIENT_ID, config.clientId);
            body.add(CLIENT_SECRET, config.clientSecret);
            body.add(REDIRECT_URI, config.redirectUri);
            body.add(CODE, code);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<HubspotTokenResponse> response = restTemplate.postForEntity(
                    config.tokenUrl,
                    request,
                    HubspotTokenResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                HubspotTokenResponse tokenResponse = response.getBody();
                tokenService.saveToken(tokenResponse);
                return TOKEN_EXCHANGE_SUCCESS_MESSAGE;
            } else {
                throw new AuthenticateException(ERROR_CHANGE_TOKEN + response.getStatusCode());
            }
        } catch (FeignException ex) {
            throw ex;
        }
    }


}
