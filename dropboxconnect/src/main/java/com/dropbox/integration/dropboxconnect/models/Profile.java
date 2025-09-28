package com.dropbox.integration.dropboxconnect.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class Profile {
    @JsonProperty("team_member_id")
    private String teamMemberId;
    
    @JsonProperty("account_id")
    private String accountId;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("email_verified")
    private boolean emailVerified;
    
    @JsonProperty("secondary_emails")
    private List<String> secondaryEmails;
    
    @JsonProperty("status")
    private Status status;
    
    @JsonProperty("name")
    private Name name;
    
    @JsonProperty("membership_type")
    private MembershipType membershipType;
    
    @JsonProperty("joined_on")
    private String joinedOn;
    
    @JsonProperty("invited_on")
    private String invitedOn;
    
    @JsonProperty("profile_photo_url")
    private String profilePhotoUrl;
    
    @JsonProperty("groups")
    private List<String> groups;
    
    @JsonProperty("member_folder_id")
    private String memberFolderId;
    
    @JsonProperty("root_folder_id")
    private String rootFolderId;
}