package com.dropbox.integration.dropboxconnect.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
	@NotBlank(message = "Client ID is required")
    private String clientId;
    
    @NotBlank(message = "Client Secret is required")
    private String clientSecret;
    
    @NotBlank(message = "Authorization code is required")
    private String code;
    
    private String redirectUri = "http://localhost:8080/callback";
}
