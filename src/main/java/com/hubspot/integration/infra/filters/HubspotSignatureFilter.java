package com.hubspot.integration.infra.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class HubspotSignatureFilter extends HttpFilter {

    private final String hubspotSecret;

    public HubspotSignatureFilter(String hubspotSecret) {
        this.hubspotSecret = hubspotSecret;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("WebhookFilter -> Start Webhook Signature Validation");

        log.info("WebhookFilter -> Capturing request body");
        CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(request);
        String requestBody = new String(wrappedRequest.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        log.info("WebhookFilter -> get X-HubSpot-Signature");
        String hubspotSignature = request.getHeader("X-HubSpot-Signature");

        log.info("WebhookFilter -> Calculating expected signature");
        String expectedSignature = "sha256=" + calculateHmacSHA256(requestBody, hubspotSecret);

        log.info("WebhookFilter -> Comparing expected signature with received signature");
        if (!expectedSignature.equals(hubspotSignature)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(wrappedRequest, response);
    }

    private String calculateHmacSHA256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error HMAC calculation", e);
        }
    }
}

