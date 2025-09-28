package com.dropbox.integration.dropboxconnect.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {

    @JsonProperty("access_token")
    private String accessToken;
    
    @JsonProperty("token_type")
    private String tokenType;
    
    @JsonProperty("expires_in")
    private Long expiresIn;
    
    @JsonProperty("refresh_token")
    private String refreshToken;
    
    @JsonProperty("scope")
    private String scope;
    
    @JsonProperty("uid")
    private String uid;
    
    @JsonProperty("team_id")
    private String teamId;
    
    private boolean success;
    private String error;
    
    public AuthResponse(String error) {
        this.error = error;
        this.success = false;
    }

    @JsonProperty("access_token")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        this.success = true;
    }
}
