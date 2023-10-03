package com.youtorial.backend.model.Comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CommentDtoOutput {

    @JsonProperty("id")
    private String id;

    @JsonProperty("author")
    private String author;

    @JsonProperty("commentText")
    private String commentText;

    @JsonProperty("createdAt")
    private String createdAt;

    @JsonProperty("videoId")
    private String videoId;

    @JsonProperty("responseTo")
    private String responseTo;

    @JsonProperty("likes")
    private List<String> likes;

    @JsonProperty("dislikes")
    private List<String> dislikes;

}
