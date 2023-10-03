package com.youtorial.backend.repository;

import com.youtorial.backend.model.Video.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VideoRepository extends MongoRepository<Video, String> {

    List<Video> findAllByOrderByUploadDateDesc();
    List<Video> findByTitleContainingIgnoreCase(String query);
    List<Video> findByCategoryContaining(List<String> categories);
    List<Video> findByAuthorOrderByUploadDateDesc(String user);
}
