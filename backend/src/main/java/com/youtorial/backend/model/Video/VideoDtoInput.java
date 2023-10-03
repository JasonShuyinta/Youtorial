package com.youtorial.backend.model.Video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class VideoDtoInput {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("url")
    private String url;

    @JsonProperty("thumbnail")
    private String thumbnail;

    @JsonProperty("duration")
    private String duration;

    @JsonProperty("category")
    private List<String> category;

    @JsonProperty("location")
    private String location;
}
