package com.hubspot.integration.rest;

import com.hubspot.integration.app.dto.command.CreateContactCommand;
import com.hubspot.integration.app.services.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<String> createContact(@RequestBody CreateContactCommand command) {
        contactService.createContact(command);
        return ResponseEntity.ok("Contato criado com sucesso");
    }
}
