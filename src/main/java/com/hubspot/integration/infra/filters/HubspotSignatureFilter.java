package com.hubspot.integration.infra.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HubspotSignatureFilter extends HttpFilter {

    private static final Logger log = LoggerFactory.getLogger(HubspotSignatureFilter.class);
    private final String hubspotSecret;

    public HubspotSignatureFilter(String hubspotSecret) {
        this.hubspotSecret = hubspotSecret;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(request);
        String body = null;
        try {
            log.info("WebhookFilter -> Start Reading body");
            body = wrappedRequest.getReader()
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            log.error("WebhookFilter -> Error on read body: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String dateTime = request.getHeader("X-HubSpot-Request-Timestamp");
        String signatureBase = method + uri + body + dateTime;

        log.info("WebhookFilter -> method: {}", method);
        log.info("WebhookFilter -> uri: {}", uri);
        log.info("WebhookFilter -> body: {}", body);
        log.info("WebhookFilter -> signatureBase: {}", signatureBase);

        String expectedSignature = calculateHmacBase64(signatureBase, hubspotSecret);
        String receivedSignature = request.getHeader("X-HubSpot-Signature-v3");
        log.error("WebhookFilter -> Expected: {}", expectedSignature);
        log.error("WebhookFilter -> Received: {}", receivedSignature);

        if (!Objects.equals(expectedSignature, receivedSignature)) {
            log.error("WebhookFilter -> Signature validation failed");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid signature");
            return;
        }

        chain.doFilter(wrappedRequest, response);
    }

    private String calculateHmacBase64(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Error on calculate HMAC", e);
        }
    }

}


