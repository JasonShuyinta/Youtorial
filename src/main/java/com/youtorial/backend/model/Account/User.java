package com.youtorial.backend.model.Account;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Document
@NoArgsConstructor
public class User implements Serializable {

    @Id
    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String username;
    private String image;
    private LocalDateTime subscriptionDate;
    private boolean enabled;
    private String confirmationCode;
    private String userCode;


}
