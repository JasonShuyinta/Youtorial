package com.youtorial.backend.model.Video;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
@NoArgsConstructor
public class Video {

    @Id
    private String id;
    private String title;
    private String description;
    private LocalDateTime uploadDate;
    private String url;
    private String thumbnail;
    private String duration;
    private String author;
    private List<String> category;
    private String tutorialId;
    private String location;

}
