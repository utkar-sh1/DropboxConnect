package com.dropbox.integration.dropboxconnect.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SharingPolicies {
    @JsonProperty("shared_folder_member_policy")
    private Policy sharedFolderMemberPolicy;
    
    @JsonProperty("shared_folder_join_policy")
    private Policy sharedFolderJoinPolicy;
    
    @JsonProperty("shared_link_create_policy")
    private Policy sharedLinkCreatePolicy;
    
    @JsonProperty("group_creation_policy")
    private Policy groupCreationPolicy;
    
    @JsonProperty("shared_folder_link_restriction_policy")
    private Policy sharedFolderLinkRestrictionPolicy;
    
    @JsonProperty("enforce_link_password_policy")
    private Policy enforceLinkPasswordPolicy;
    
    @JsonProperty("default_link_expiration_days_policy")
    private Policy defaultLinkExpirationDaysPolicy;
    
    @JsonProperty("shared_link_default_permissions_policy")
    private Policy sharedLinkDefaultPermissionsPolicy;
}

@Data
class Policy {
    @JsonProperty(".tag")
    private String tag;
}
