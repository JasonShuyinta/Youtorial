package com.youtorial.backend.controller;

import com.youtorial.backend.model.Comment.Comment;
import com.youtorial.backend.model.Comment.CommentConverter;
import com.youtorial.backend.model.Comment.CommentDtoInput;
import com.youtorial.backend.model.Comment.CommentDtoOutput;
import com.youtorial.backend.service.CommentService;
import com.youtorial.backend.utils.exception.ExpiredTokenException;
import com.youtorial.backend.utils.exception.InvalidTokenException;
import com.youtorial.backend.utils.exception.NotFoundsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.youtorial.backend.utils.Constants.*;
import static org.springframework.http.ResponseEntity.*;

@Controller
@RequestMapping("/api/v1/comment")
@Slf4j
@CrossOrigin("*")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentConverter commentConverter;

    @PostMapping("/createComment")
    public ResponseEntity<CommentDtoOutput> saveComment(@RequestBody CommentDtoInput input, @RequestHeader("Authorization") String authToken) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Comment comment = commentConverter.dtoInputToEntity(input);
            Comment c = commentService.createComment(comment, authToken);
            CommentDtoOutput output = commentConverter.entityToDtoOutput(c);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(output);
        } catch (InvalidTokenException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (ExpiredTokenException ex) {
            log.error(ex + this.getClass().getSimpleName(), HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @GetMapping("/getComments/{videoId}")
    public ResponseEntity<List<CommentDtoOutput>> getComments(@PathVariable String videoId) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            List<Comment> commentList = commentService.getComments(videoId);
            List<CommentDtoOutput> output = new ArrayList<>();
            if (!commentList.isEmpty()) {
                for (Comment c : commentList) {
                    output.add(commentConverter.entityToDtoOutput(c));
                }
                log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return ok(output);
            } else return noContent().build();
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @GetMapping("/getSingleComment/{commentId}")
    public ResponseEntity<CommentDtoOutput> getComment(@PathVariable String commentId) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Comment comment = commentService.getCommentById(commentId);
            CommentDtoOutput output = commentConverter.entityToDtoOutput(comment);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(output);
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }



    @DeleteMapping("/{commentId}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable String commentId,
                                                 @RequestHeader("Authorization") String authToken) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            commentService.deleteComment(commentId, authToken);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok().build();
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InvalidTokenException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (ExpiredTokenException ex) {
            log.error(ex + this.getClass().getSimpleName(), HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable String commentId,
                                              @RequestBody CommentDtoInput input,
                                              @RequestHeader("Authorization") String authToken) {
        log.info("Comment id {}, commentText {} ", commentId, input);
        try {
            commentService.updateComment(commentId, input.getCommentText(), authToken);
            return ok().build();
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InvalidTokenException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (ExpiredTokenException ex) {
            log.error(ex + this.getClass().getSimpleName(), HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }


    /*****************************************************************************/
    @GetMapping("/likeComment/{commentId}")
    public ResponseEntity<Boolean> likeComment(@PathVariable String commentId,
                                               @RequestHeader("Authorization") String authToken) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            boolean isLiked = commentService.likeComment(commentId, authToken);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(isLiked);
        } catch (InvalidTokenException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (ExpiredTokenException ex) {
            log.error(ex + this.getClass().getSimpleName(), HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR);
            return internalServerError().build();
        }
    }

    @GetMapping("/dislikeComment/{commentId}")
    public ResponseEntity<Boolean> dislikeComment(@PathVariable String commentId,
                                                  @RequestHeader("Authorization") String authToken) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            boolean isDisliked = commentService.dislikeComment(commentId, authToken);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(isDisliked);
        } catch (InvalidTokenException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (ExpiredTokenException ex) {
            log.error(ex + this.getClass().getSimpleName(), HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR);
            return internalServerError().build();
        }
    }
    /*****************************************************************************/


}
