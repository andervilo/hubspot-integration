package com.hubspot.integration.app.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubspotSignatureService {

    @Value("${hubspot.client-secret}")
    private String hubspotSecret;

    private final RequestBodyReaderService bodyReaderService;

    public boolean isValidSignature(HttpServletRequest request) throws IOException {
        log.info("ValidSignature -> Validating signature start");

        String method = request.getMethod();
        String path = request.getRequestURI();

        String body = bodyReaderService.readBody(request);

        String signatureBase = method + path + body;

        String calculatedSignature = calculateHmacBase64(signatureBase, hubspotSecret);
        String receivedSignature = request.getHeader("X-HubSpot-Signature-v3");

        log.info("ValidSignature -> Calculated signature: {}", calculatedSignature);
        log.info("ValidSignature -> Received signature: {}", receivedSignature);
        log.info("ValidSignature -> Request body: {}", body);
        log.info("ValidSignature -> Request method: {}", method);
        log.info("ValidSignature -> Request path: {}", path);

        try {
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
