package com.hubspot.integration.app.dto.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateContactCommand {
    private String email;
    private String firstname;
    private String lastname;
}
