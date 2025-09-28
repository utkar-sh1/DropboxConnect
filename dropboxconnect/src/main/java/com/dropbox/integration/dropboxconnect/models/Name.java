package com.dropbox.integration.dropboxconnect.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Name {
    @JsonProperty("given_name")
    private String givenName;
    
    @JsonProperty("surname")
    private String surname;
    
    @JsonProperty("familiar_name")
    private String familiarName;
    
    @JsonProperty("display_name")
    private String displayName;
    
    @JsonProperty("abbreviated_name")
    private String abbreviatedName;
}