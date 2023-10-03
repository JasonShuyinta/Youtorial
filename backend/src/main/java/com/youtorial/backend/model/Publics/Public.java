package com.youtorial.backend.model.Publics;

import com.youtorial.backend.model.Step.Step;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@NoArgsConstructor
public class Public {

    @Id
    private String id;
    private String author;
    private String videoId;
    private List<Step> steps;
    private String tutorialId;
    private List<String> userRating;

}
