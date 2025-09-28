package com.dropbox.integration.dropboxconnect.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Asset {
    @JsonProperty(".tag")
    private String tag;
    
    private Path path;
    
    @JsonProperty("display_name")
    private String displayName;
}