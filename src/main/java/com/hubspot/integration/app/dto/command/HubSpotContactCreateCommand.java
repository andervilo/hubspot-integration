package com.hubspot.integration.app.dto.command;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class HubSpotContactCreateCommand {
    private Map<String, Object> properties = new HashMap<>();

    public static HubSpotContactCreateCommand from(CreateContactCommand command) {
        HubSpotContactCreateCommand contact = new HubSpotContactCreateCommand();
        contact.getProperties().put("email", command.getEmail());
        contact.getProperties().put("firstname", command.getFirstname());
        contact.getProperties().put("lastname", command.getLastname());
        return contact;
    }
}
