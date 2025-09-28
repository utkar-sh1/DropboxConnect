package com.dropbox.integration.dropboxconnect.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NamespaceRelative {
    @JsonProperty("ns_id")
    private String nsId;
    
    @JsonProperty("is_shared_namespace")
    private boolean isSharedNamespace;
}