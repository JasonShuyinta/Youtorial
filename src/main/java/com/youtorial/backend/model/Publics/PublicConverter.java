package com.youtorial.backend.model.Publics;


import org.springframework.stereotype.Component;

@Component
public class PublicConverter {

    public Public dtoInputToEntity(PublicDtoInput input) {
        Public pub = new Public();
        pub.setId(input.getId());
        pub.setAuthor(input.getAuthor());
        pub.setSteps(input.getSteps());
        pub.setTutorialId(input.getTutorialId());
        pub.setUserRating(input.getUserRating());
        return pub;
    }

    public PublicDtoOutput entityToDtoOutput(Public pub) {
        PublicDtoOutput output = new PublicDtoOutput();
        output.setId(pub.getId());
        output.setSteps(pub.getSteps());
        output.setAuthor(pub.getAuthor());
        output.setTutorialId(pub.getTutorialId());
        output.setUserRating(pub.getUserRating());
        return output;
    }
}
