package com.dropbox.integration.dropboxconnect.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TeamInfo {
	@JsonProperty("name")
    private String name;
    
    @JsonProperty("team_id")
    private String teamId;
    
    @JsonProperty("num_licensed_users")
    private Integer numLicensedUsers;
    
    @JsonProperty("num_provisioned_users")
    private Integer numProvisionedUsers;
    
    @JsonProperty("num_used_licenses")
    private Integer numUsedLicenses;
    
    @JsonProperty("policies")
    private TeamPolicies policies;
}
