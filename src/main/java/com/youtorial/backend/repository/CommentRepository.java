package com.youtorial.backend.repository;

import com.youtorial.backend.model.Comment.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, String> {

    Optional<List<Comment>> findCommentsByVideoId(String videoId);

    void deleteByVideoId(String videoId);
}
