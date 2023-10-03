package com.youtorial.backend.repository;

import com.youtorial.backend.model.Step.Step;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StepRepository extends MongoRepository<Step, String> {
    void deleteByVideoId(String videoId);
}
