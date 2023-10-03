package com.youtorial.backend.repository;

import com.youtorial.backend.model.Account.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByConfirmationCode(String verificationCode);
}
