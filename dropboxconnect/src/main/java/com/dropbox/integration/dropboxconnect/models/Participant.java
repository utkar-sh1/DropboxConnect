package com.dropbox.integration.dropboxconnect.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Participant {
    @JsonProperty(".tag")
    private String tag;
    
    @JsonProperty("group_id")
    private String groupId;
    
    @JsonProperty("display_name")
    private String displayName;
}