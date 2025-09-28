package com.dropbox.integration.dropboxconnect.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EventType {
    @JsonProperty(".tag")
    private String tag;
    
    private String description;
}