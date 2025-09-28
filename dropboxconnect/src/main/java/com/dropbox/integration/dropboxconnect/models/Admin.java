package com.dropbox.integration.dropboxconnect.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Admin {
    @JsonProperty(".tag")
    private String tag;
    
    @JsonProperty("account_id")
    private String accountId;
    
    @JsonProperty("display_name")
    private String displayName;
    
    private String email;
    
    @JsonProperty("team_member_id")
    private String teamMemberId;
}