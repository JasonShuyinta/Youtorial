package com.youtorial.backend.model.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Notes {

    @JsonProperty("notes")
    private String note;

    @JsonProperty("timestamp")
    private String timestamp;

}
