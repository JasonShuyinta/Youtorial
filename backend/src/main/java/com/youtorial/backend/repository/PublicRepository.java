package com.youtorial.backend.repository;

import com.youtorial.backend.model.Publics.Public;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PublicRepository extends MongoRepository<Public, String> {

    Public findByTutorialId(String tutorialId);
    Public findPublicByTutorialId(String tutorialId);
    Optional<Public> findPublicByVideoId(String videoId);
    List<Public> findPublicByAuthor(String user);
    void deleteByVideoId(String videoId);
}
