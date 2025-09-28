package com.dropbox.integration.dropboxconnect.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DisplayName {
	@JsonProperty("display_name")
    private String displayName;
    
    @JsonProperty("given_name")
    private String givenName;
    
    @JsonProperty("surname")
    private String surname;
}
