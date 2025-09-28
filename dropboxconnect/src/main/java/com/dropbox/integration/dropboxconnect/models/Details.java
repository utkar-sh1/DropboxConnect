package com.dropbox.integration.dropboxconnect.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Details {
    @JsonProperty(".tag")
    private String tag;
    
    @JsonProperty("shared_content_access_level")
    private Tag sharedContentAccessLevel;
}