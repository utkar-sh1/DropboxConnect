package com.dropbox.integration.dropboxconnect.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Member {
    @JsonProperty("profile")
    private Profile profile;
    
    @JsonProperty("role")
    private Role role;
}
