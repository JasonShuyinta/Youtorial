package com.youtorial.backend.model.Interaction;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document
public class UserInteraction {

    @Id
    private String id;
    private String userId;
    private List<VideoInteraction> videoInteractionList;
    private List<String> followersId;
    private List<String> subscriptions;
}
