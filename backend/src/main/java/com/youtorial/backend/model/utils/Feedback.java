package com.youtorial.backend.model.utils;

import com.youtorial.backend.model.Account.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document
@NoArgsConstructor
public class Feedback implements Serializable {

    @Id
    private String id;
    private String goalAchieved;
    private Integer satisfaction;
    private String suggestions;
    private User user;
}
