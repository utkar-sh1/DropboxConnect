package com.dropbox.integration.dropboxconnect.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class Status {
    @JsonProperty(".tag")
    private String tag;
}