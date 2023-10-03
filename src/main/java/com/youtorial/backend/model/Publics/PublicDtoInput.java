package com.youtorial.backend.model.Publics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.youtorial.backend.model.Step.Step;
import lombok.Data;

import java.util.List;

@Data
public class PublicDtoInput {

    @JsonProperty("id")
    private String id;
    @JsonProperty("author")
    private String author;
    @JsonProperty("steps")
    private List<Step> steps;
    @JsonProperty("tutorialId")
    private String tutorialId;
    @JsonProperty("userRating")
    private List<String> userRating;
}
