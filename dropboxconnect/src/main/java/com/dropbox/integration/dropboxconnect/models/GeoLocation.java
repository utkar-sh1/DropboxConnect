package com.dropbox.integration.dropboxconnect.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GeoLocation {
    private String city;
    private String region;
    private String country;
    
    @JsonProperty("ip_address")
    private String ipAddress;
}