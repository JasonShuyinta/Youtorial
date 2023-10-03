package com.youtorial.backend.model.Interaction;

import com.youtorial.backend.model.utils.Notes;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document
public class StepInteraction {

    @Id
    private String id;
    private String stepId;
    private Boolean isChecked;
    private Notes notes;
}
