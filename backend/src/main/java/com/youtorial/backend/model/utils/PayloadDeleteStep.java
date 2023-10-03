package com.youtorial.backend.model.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayloadDeleteStep {

    @JsonProperty("stepId")
    private String stepId;

    @JsonProperty("tutorialId")
    private String tutorialId;

    @JsonProperty("videoId")
    private String videoId;
}
