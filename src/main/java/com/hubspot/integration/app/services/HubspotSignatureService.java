package com.hubspot.integration.app.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HubspotSignatureService {

    @Value("${hubspot.client-secret}")
    private String hubspotSecret;

    public boolean isValidSignature(HttpServletRequest request) {
        try {
            String method = request.getMethod();
            String path = request.getRequestURI();

            String body = request.getReader()
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));

            String signatureBase = method + path + body;

            String calculatedSignature = calculateHmacBase64(signatureBase, hubspotSecret);
            String receivedSignature = request.getHeader("X-HubSpot-Signature-v3");

            log.info("Calculated signature: {}", calculatedSignature);
            log.info("Received signature: {}", receivedSignature);
            log.info("Request body: {}", body);
            log.info("Request method: {}", method);
            log.info("Request path: {}", path);

            return Objects.equals(calculatedSignature, receivedSignature);
        } catch (Exception e) {
            return false;
        }
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
