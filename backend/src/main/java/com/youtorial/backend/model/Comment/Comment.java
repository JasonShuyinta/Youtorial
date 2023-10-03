package com.youtorial.backend.model.Comment;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
@Document
public class Comment implements Serializable {

    @Id
    private String id;
    private String author;
    private String commentText;
    private LocalDateTime createdAt;
    private String videoId;
    private String responseTo;
    private List<String> likes;
    private List<String> dislikes;
}

