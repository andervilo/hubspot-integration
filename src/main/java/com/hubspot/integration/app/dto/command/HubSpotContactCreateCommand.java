package com.hubspot.integration.app.dto.command;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

import static com.hubspot.integration.app.constants.AppConstants.*;

@Getter
@Setter
public class HubSpotContactCreateCommand {

    private Map<String, Object> properties = new HashMap<>();

    public static HubSpotContactCreateCommand from(CreateContactCommand command) {
        HubSpotContactCreateCommand contact = new HubSpotContactCreateCommand();
        contact.getProperties().put(EMAIL, command.getEmail());
        contact.getProperties().put(FIRSTNAME, command.getFirstname());
        contact.getProperties().put(LASTNAME, command.getLastname());
        return contact;
    }
}
