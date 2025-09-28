package com.dropbox.integration.dropboxconnect.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Origin {
    @JsonProperty("geo_location")
    private GeoLocation geoLocation;
    
    @JsonProperty("access_method")
    private AccessMethod accessMethod;
}