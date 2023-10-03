package com.youtorial.backend.repository;

import com.youtorial.backend.model.Interaction.VideoRating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VideoRatingRepository extends MongoRepository<VideoRating, String> {

    Optional<VideoRating> findVideoRatingByVideoId(String videoId);
}
