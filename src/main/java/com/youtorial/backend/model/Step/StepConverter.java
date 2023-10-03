package com.youtorial.backend.model.Step;

import org.springframework.stereotype.Component;

@Component
public class StepConverter {

    public Step dtoInputToEntity(StepDtoInput input) {
        Step step = new Step();
        step.setId(input.getId());
        step.setTitle(input.getTitle());
        step.setEndTime(input.getEndTime());
        step.setStartTime(input.getStartTime());
        step.setLinks(input.getLinks());
        step.setInstruction(input.getInstruction());
        step.setCode(input.getCode());
        step.setLanguage(input.getLanguage());
        step.setImage(input.getImage());
        step.setIngredients(input.getIngredients());
        step.setNumPeople(input.getNumPeople());
        step.setFormula(input.getFormula());
        step.setAudioFile(input.getAudioFile());
        step.setAudioName(input.getAudioName());
        step.setVideoId(input.getVideoId());
        return step;
    }

    public StepDtoOutput entityToDtoOutput(Step step) {
        StepDtoOutput output = new StepDtoOutput();
        output.setId(step.getId());
        output.setUploadDate(step.getUploadDate().toString());
        output.setTitle(step.getTitle());
        output.setEndTime(step.getEndTime());
        output.setStartTime(step.getStartTime());
        output.setLinks(step.getLinks());
        output.setInstruction(step.getInstruction());
        output.setCode(step.getCode());
        output.setLanguage(step.getLanguage());
        output.setImage(step.getImage());
        output.setIngredients(step.getIngredients());
        output.setNumPeople(step.getNumPeople());
        output.setFormula(step.getFormula());
        output.setAudioFile(step.getAudioFile());
        output.setAudioName(step.getAudioName());
        output.setVideoId(step.getVideoId());
        return output;

    }
}
