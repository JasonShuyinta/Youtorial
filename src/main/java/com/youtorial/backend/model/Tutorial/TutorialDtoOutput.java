package com.youtorial.backend.model.Tutorial;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.youtorial.backend.model.Step.Step;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TutorialDtoOutput implements Serializable {

    @JsonProperty("id")
    private String id;
    @JsonProperty("videoId")
    private String videoId;
    @JsonProperty("author")
    private String author;
    @JsonProperty("steps")
    private List<Step> steps;

}
