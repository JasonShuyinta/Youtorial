package com.youtorial.backend.repository;

import com.youtorial.backend.model.Interaction.UserInteraction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserInteractionRepository extends MongoRepository<UserInteraction, String> {
    Optional<UserInteraction> findUserInteractionByUserId(String userId);
}
