package com.hubspot.integration.infra.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
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

        // Encapsula o request para poder ler o corpo várias vezes
        CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(request);
        String body = new String(wrappedRequest.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String signatureBase = method + uri + body;

        String expectedSignature = calculateHmacHex(signatureBase, hubspotSecret);
        String receivedSignature = request.getHeader("X-HubSpot-Signature-v3");

        if (!Objects.equals(expectedSignature, receivedSignature)) {
            log.error("WebhookFilter -> Signature validation failed");
            log.error("WebhookFilter -> Expected: {}", expectedSignature);
            log.error("WebhookFilter -> Received: {}", receivedSignature);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Assinatura inválida");
            return;
        }

        chain.doFilter(wrappedRequest, response);
    }

    private String calculateHmacHex(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular HMAC", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}


