package com.youtorial.backend.model.Interaction;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document
public class VideoRating {

    @Id
    private String id;
    private String videoId;
    private Integer totalRating;
    private Integer averageRating;
    private Integer ratingNum;
}
