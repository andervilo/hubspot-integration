package com.hubspot.integration.rest;

import com.hubspot.integration.app.dto.response.HubspotTokenResponse;
import com.hubspot.integration.app.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/url")
    public String getAuthorizationUrl() {
        return authService.generateAuthorizationUrl();
    }

    @GetMapping("/callback")
    public ResponseEntity<HubspotTokenResponse> handleCallback(@RequestParam String code) {
        return ResponseEntity.ok(authService.exchangeCodeForToken(code));
    }
}
