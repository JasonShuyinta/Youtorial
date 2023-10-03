package com.youtorial.backend.repository;


import com.youtorial.backend.model.Tutorial.Tutorial;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TutorialRepository extends MongoRepository<Tutorial, String> {

    Optional<Tutorial> findTutorialByVideoId(String videoId);

    void deleteByVideoId(String videoId);
}
