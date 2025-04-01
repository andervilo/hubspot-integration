package com.hubspot.integration.infra.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuthConfig {

    @Value("${hubspot.client-id}")
    public String clientId;

    @Value("${hubspot.client-secret}")
    public String clientSecret;

    @Value("${hubspot.redirect-uri}")
    public String redirectUri;

    @Value("${hubspot.scopes}")
    public String scopes;

    @Value("${hubspot.auth-url}")
    public String authUrl;

    @Value("${hubspot.token-url}")
    public String tokenUrl;

    @Value("${hubspot.api-url}")
    public String apiUrl;
}
