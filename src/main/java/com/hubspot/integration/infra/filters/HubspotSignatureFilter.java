package com.hubspot.integration.infra.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
public class HubspotSignatureFilter extends HttpFilter {

    public static final String X_HUB_SPOT_REQUEST_TIMESTAMP = "X-HubSpot-Request-Timestamp";
    public static final String X_HUB_SPOT_SIGNATURE_V_3 = "X-HubSpot-Signature-v3";
    public static final String HOST = "host";
    public static final String HTTP_PROTOCOL = "https://";
    @Value("${hubspot.client-secret}")
    private String hubspotSecret;

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
        String dateTime = request.getHeader(X_HUB_SPOT_REQUEST_TIMESTAMP);
        String host = HTTP_PROTOCOL + request.getHeader(HOST);
        String signatureBase = method + host + uri + body + dateTime;

        String expectedSignature = calculateHmacBase64(signatureBase, hubspotSecret);
        String receivedSignature = request.getHeader(X_HUB_SPOT_SIGNATURE_V_3);


        if (!Objects.equals(expectedSignature, receivedSignature)) {
            log.error("WebhookFilter -> Signature validation failed");
            log.error("WebhookFilter -> Expected: {}", expectedSignature);
            log.error("WebhookFilter -> Received: {}", receivedSignature);
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


