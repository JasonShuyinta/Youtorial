package com.youtorial.backend.model.Tutorial;

import com.youtorial.backend.model.Step.Step;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@Document
@NoArgsConstructor
public class Tutorial implements Serializable {

    @Id
    private String id;
    private String videoId;
    private String author;
    private List<Step> steps;
}
