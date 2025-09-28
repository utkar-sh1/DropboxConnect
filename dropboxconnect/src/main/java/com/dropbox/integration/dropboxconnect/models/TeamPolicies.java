package com.dropbox.integration.dropboxconnect.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TeamPolicies {
        @JsonProperty("sharing")
        private SharingPolicies sharing;
        
        @JsonProperty("emm_state")
        private Policy emmState;
        
        @JsonProperty("office_addin")
        private Policy officeAddin;
        
        @JsonProperty("suggest_members_policy")
        private Policy suggestMembersPolicy;
        
        @JsonProperty("top_level_content_policy")
        private Policy topLevelContentPolicy;
}
