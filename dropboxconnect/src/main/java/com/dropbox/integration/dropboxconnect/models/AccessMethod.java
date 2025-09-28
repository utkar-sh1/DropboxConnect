package com.dropbox.integration.dropboxconnect.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AccessMethod {
    @JsonProperty(".tag")
    private String tag;
    
    @JsonProperty("session_id")
    private String sessionId;
}