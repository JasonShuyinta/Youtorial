package com.youtorial.backend.model.Account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDtoOutput implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("image")
    private String image;

    @JsonProperty("subscriptionDate")
    private String subscriptionDate;

    @JsonProperty("userCode")
    private String userCode;

    @JsonProperty("enabled")
    private boolean enabled;
}
