package com.youtorial.backend.model.Tutorial;

import org.springframework.stereotype.Component;

@Component
public class TutorialConverter {

    public Tutorial dtoInputToEntity(TutorialDtoInput input) {
        Tutorial tutorial = new Tutorial();
        tutorial.setId(input.getId());
        tutorial.setVideoId(input.getVideoId());
        tutorial.setAuthor(input.getAuthor());
        tutorial.setSteps(input.getSteps());
        return tutorial;
    }

    public TutorialDtoOutput entityToDtoOutput(Tutorial tutorial) {
        TutorialDtoOutput output = new TutorialDtoOutput();
        output.setId(tutorial.getId());
        output.setVideoId(tutorial.getVideoId());
        output.setAuthor(tutorial.getAuthor());
        output.setSteps(tutorial.getSteps());
        return output;
    }
}
