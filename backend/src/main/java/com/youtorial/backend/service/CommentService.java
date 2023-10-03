package com.youtorial.backend.service;

import com.youtorial.backend.model.Account.User;
import com.youtorial.backend.model.Comment.Comment;
import com.youtorial.backend.repository.CommentRepository;
import com.youtorial.backend.utils.exception.ExpiredTokenException;
import com.youtorial.backend.utils.exception.InvalidTokenException;
import com.youtorial.backend.utils.exception.NotFoundsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.youtorial.backend.utils.Constants.*;

@Slf4j
@Service
@Component
@RequiredArgsConstructor
public class CommentService {

    private final AuthService authService;
    private final CommentRepository commentRepository;

    public Comment createComment(Comment comment, String authToken) throws InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            User author = authService.getUserByAuthToken(authToken);
            comment.setAuthor(author.getId());
            comment.setCreatedAt(LocalDateTime.now());
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return commentRepository.save(comment);
        } catch (InvalidTokenException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new InvalidTokenException(e.getMessage());
        } catch (ExpiredTokenException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new ExpiredTokenException(e.getMessage());
        } catch (Exception e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName());
        }
    }

    public List<Comment> getComments(String videoId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        return commentRepository.findCommentsByVideoId(videoId).orElseThrow(() -> new NotFoundsException("Could not find comment with videoId " + videoId));
    }

    public Comment getCommentById(String commentId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        return commentRepository.findById(commentId).orElseThrow(() -> new NotFoundsException("Could not find comment with id " + commentId));
    }


    public void deleteComment(String commentId, String authToken) throws NotFoundsException, InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            User user = authService.getUserByAuthToken(authToken);
            if (user != null) {
                if (!commentRepository.existsById(commentId)) {
                    log.error(NOT_FOUND_ERROR + this.getClass());
                    throw new NotFoundsException(NO_TUTORIAL_WAS_FOUND);
                }
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                commentRepository.deleteById(commentId);
            } else {
                log.error("Could not find user in {}", this.getClass().getSimpleName());
                throw new NotFoundsException("Could not find user with authToken " + authToken);
            }
        } catch (NotFoundsException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (InvalidTokenException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new InvalidTokenException(e.getMessage());
        } catch (ExpiredTokenException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new ExpiredTokenException(e.getMessage());
        } catch (Exception e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName());
        }
    }


    public void updateComment(String commentId, String commentText, String authToken) throws NotFoundsException, InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            User user = authService.getUserByAuthToken(authToken);
            Optional<Comment> optionalComment = commentRepository.findById(commentId);
            if (optionalComment.isPresent()) {
                Comment comment = optionalComment.get();
                if (comment.getAuthor().equals(user.getId())) {
                    comment.setCommentText(commentText);
                    log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                    commentRepository.save(comment);
                }
            } else {
                log.error("404 Error");
                throw new NotFoundsException("Could not find comment with id " + commentId);
            }
        } catch (NotFoundsException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (InvalidTokenException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new InvalidTokenException(e.getMessage());
        } catch (ExpiredTokenException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new ExpiredTokenException(e.getMessage());
        } catch (Exception e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName());
        }
    }

    public void deleteCommentByVideoId(String videoId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            commentRepository.deleteByVideoId(videoId);
        } catch (NotFoundsException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (Exception e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName());
        }
    }


    /***********************UNUSED SERVICES *****************************/
    public boolean dislikeComment(String commentId, String authToken) throws InvalidTokenException, ExpiredTokenException {
        try {
            boolean isDisliked = false;
            int dislikedIndexPosition = 0;
            List<String> dislikes;
            User author = authService.getUserByAuthToken(authToken);
            Comment comment = getCommentById(commentId);
            if (comment.getDislikes() != null && !comment.getDislikes().isEmpty()) {
                dislikes = comment.getLikes();
                for (int i = 0; i < dislikes.size(); i++) {
                    if (dislikes.get(i).equals(author.getId())) {
                        isDisliked = true;
                        dislikedIndexPosition = i;
                        break;
                    }
                }
                if (isDisliked) dislikes.remove(dislikedIndexPosition);
                else dislikes.add(author.getId());
            } else {
                dislikes = new ArrayList<>();
                dislikes.add(author.getId());
            }
            comment.setDislikes(dislikes);
            commentRepository.save(comment);
            return isDisliked;
        } catch (InvalidTokenException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new InvalidTokenException(e.getMessage());
        } catch (ExpiredTokenException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new ExpiredTokenException(e.getMessage());
        } catch (Exception e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName());
        }
    }

    public boolean likeComment(String commentId, String authToken) throws InvalidTokenException, ExpiredTokenException {
        try {
            boolean isLiked = false;
            int likedIndexPosition = 0;
            List<String> likes;
            User author = authService.getUserByAuthToken(authToken);
            Comment comment = getCommentById(commentId);
            if (comment.getLikes() != null && !comment.getLikes().isEmpty()) {
                likes = comment.getLikes();
                for (int i = 0; i < likes.size(); i++) {
                    if (likes.get(i).equals(author.getId())) {
                        isLiked = true;
                        likedIndexPosition = i;
                        break;
                    }
                }
                if (isLiked) likes.remove(likedIndexPosition);
                else likes.add(author.getId());
            } else {
                likes = new ArrayList<>();
                likes.add(author.getId());
            }
            comment.setLikes(likes);
            commentRepository.save(comment);
            return isLiked;
        } catch (InvalidTokenException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new InvalidTokenException(e.getMessage());
        } catch (ExpiredTokenException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new ExpiredTokenException(e.getMessage());
        } catch (Exception e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName());
        }
    }
    /***************************************************************************/

}
