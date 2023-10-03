package com.youtorial.backend.repository;

import com.youtorial.backend.model.utils.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedbackRepository extends MongoRepository<Feedback, String> {
}
