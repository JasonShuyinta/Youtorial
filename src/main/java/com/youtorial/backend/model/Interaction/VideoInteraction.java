package com.youtorial.backend.model.Interaction;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document
public class VideoInteraction {

    @Id
    private String id;
    private String videoId;
    private List<StepInteraction> stepInteractionList;
    private Boolean isSaved;
    private Integer rating;
}
