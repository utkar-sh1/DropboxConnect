package com.dropbox.integration.dropboxconnect.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "dropbox.api")
public class DropboxConfig {
	private String baseUrl;
    private String authUrl;
    private String scopes;
    private Endpoints endpoints = new Endpoints();
    
}
