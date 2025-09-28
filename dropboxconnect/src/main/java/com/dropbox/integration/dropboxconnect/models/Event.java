package com.dropbox.integration.dropboxconnect.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Data;

@Data
public class Event {
    private String timestamp;
    
    @JsonProperty("event_category")
    private Tag eventCategory;
    
    private Actor actor;
    private Origin origin;
    
    @JsonProperty("involve_non_team_member")
    private boolean involveNonTeamMember;
    
    private Tag context;
    private List<Participant> participants;
    private List<Asset> assets;
    
    @JsonProperty("event_type")
    private EventType eventType;
    
    private Details details;
}
