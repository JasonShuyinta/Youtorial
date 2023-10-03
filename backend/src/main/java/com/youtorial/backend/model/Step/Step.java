package com.youtorial.backend.model.Step;

import com.youtorial.backend.model.utils.Ingredient;
import com.youtorial.backend.model.utils.Link;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Document
@NoArgsConstructor
public class Step implements Serializable {

    @Id
    private String id;
    private LocalDateTime uploadDate;
    private String title;
    private String endTime;
    private String startTime;
    private Link[] links;
    private String instruction;
    private String code;
    private String language;
    private String image;
    private Ingredient[] ingredients;
    private int numPeople;
    private String formula;
    private String audioFile;
    private String audioName;
    private String videoId;

}
