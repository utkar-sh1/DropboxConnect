package com.dropbox.integration.dropboxconnect.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthUrlResponse {
	private String authUrl;
	private String state;

}
