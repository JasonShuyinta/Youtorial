package com.youtorial.backend.model.Comment;

import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

    public Comment dtoInputToEntity(CommentDtoInput input) {
        Comment comment = new Comment();
        comment.setId(input.getId());
        comment.setAuthor(input.getAuthor());
        comment.setCommentText(input.getCommentText());
        comment.setVideoId(input.getVideoId());
        comment.setResponseTo(input.getResponseTo());
        comment.setLikes(input.getLikes());
        comment.setDislikes(input.getDislikes());
        return comment;
    }

    public CommentDtoOutput entityToDtoOutput(Comment comment) {
        CommentDtoOutput dto = new CommentDtoOutput();
        dto.setId(comment.getId());
        dto.setAuthor(comment.getAuthor());
        dto.setCommentText(comment.getCommentText());
        dto.setCreatedAt(comment.getCreatedAt().toString());
        dto.setVideoId(comment.getVideoId());
        dto.setResponseTo(comment.getResponseTo());
        dto.setLikes(comment.getLikes());
        dto.setDislikes(comment.getDislikes());
        return dto;
    }
}
