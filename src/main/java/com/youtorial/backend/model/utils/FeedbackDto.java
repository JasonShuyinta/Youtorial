package com.youtorial.backend.model.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FeedbackDto {

    @JsonProperty("goalAchieved")
    private String goalAchieved;

    @JsonProperty("satisfaction")
    private Integer satisfaction;

    @JsonProperty("suggestions")
    private String suggestions;
}
