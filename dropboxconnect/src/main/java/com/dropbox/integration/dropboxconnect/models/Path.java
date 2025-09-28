package com.dropbox.integration.dropboxconnect.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Path {
    @JsonProperty("namespace_relative")
    private NamespaceRelative namespaceRelative;
}