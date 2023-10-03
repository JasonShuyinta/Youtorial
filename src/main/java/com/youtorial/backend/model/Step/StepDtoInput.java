package com.youtorial.backend.model.Step;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.youtorial.backend.model.utils.Ingredient;
import com.youtorial.backend.model.utils.Link;
import lombok.Data;

import java.io.Serializable;

@Data
public class StepDtoInput implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("endTime")
    private String endTime;

    @JsonProperty("startTime")
    private String startTime;

    @JsonProperty("links")
    private Link[] links;

    @JsonProperty("instruction")
    private String instruction;

    @JsonProperty("code")
    private String code;

    @JsonProperty("language")
    private String language;

    @JsonProperty("image")
    private String image;

    @JsonProperty("ingredients")
    private Ingredient[] ingredients;

    @JsonProperty("numPeople")
    private int numPeople;

    @JsonProperty("formula")
    private String formula;

    @JsonProperty("audioFile")
    private String audioFile;

    @JsonProperty("audioName")
    private String audioName;

    @JsonProperty("videoId")
    private String videoId;
}
