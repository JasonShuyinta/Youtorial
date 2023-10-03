package com.youtorial.backend.model.Account;

import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public User dtoInputToEntity(UserDtoInput userDtoInput) {
        User user = new User();
        user.setId(userDtoInput.getId());
        user.setEmail(userDtoInput.getEmail());
        user.setPassword(userDtoInput.getPassword());
        user.setFirstName(userDtoInput.getFirstName());
        user.setLastName(userDtoInput.getLastName());
        user.setUsername(userDtoInput.getUsername());
        user.setImage(userDtoInput.getImage());

        return user;
    }

    public UserDtoOutput entityToDtoOutput(User user) {
        UserDtoOutput userDtoOutput = new UserDtoOutput();
        userDtoOutput.setId(user.getId());
        userDtoOutput.setEmail(user.getEmail());
        userDtoOutput.setFirstName(user.getFirstName());
        userDtoOutput.setLastName(user.getLastName());
        userDtoOutput.setUsername(user.getUsername());
        userDtoOutput.setSubscriptionDate(user.getSubscriptionDate().toString());
        userDtoOutput.setImage(user.getImage());
        userDtoOutput.setUserCode(user.getUserCode());
        userDtoOutput.setEnabled(user.isEnabled());
        return userDtoOutput;
    }
}
