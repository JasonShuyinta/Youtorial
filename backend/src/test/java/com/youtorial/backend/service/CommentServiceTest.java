package com.youtorial.backend.service;

import com.youtorial.backend.BaseTest;
import com.youtorial.backend.model.Account.User;
import com.youtorial.backend.model.Comment.Comment;
import com.youtorial.backend.utils.exception.ExpiredTokenException;
import com.youtorial.backend.utils.exception.InvalidTokenException;
import com.youtorial.backend.utils.exception.NotFoundsException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class CommentServiceTest extends BaseTest {

    @Autowired
    CommentService commentService;
    @MockBean
    AuthService authService;
    @Autowired
    UserService userService;

    @Test
    void createComment() throws Exception {
        Comment comment = new Comment();
        String authToken = "Token";
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        Comment createdComment = commentService.createComment(comment, authToken);
        assertNotNull(createdComment);

        try {
            commentService.deleteComment(createdComment.getId(), authToken);
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    void createCommentInvalidKO() throws Exception {
        Comment comment = new Comment();
        String authToken = "Token";
        try {
            when(authService.getUserByAuthToken(ArgumentMatchers.any()))
                    .thenThrow(new InvalidTokenException(INVALID_TOKEN));
            commentService.createComment(comment, authToken);
        } catch (InvalidTokenException e) {
            assertNotNull(e);
        }
    }

    @Test
    void createCommentExpiredKO() throws Exception {
        Comment comment = new Comment();
        String authToken = "Token";
        try {
            when(authService.getUserByAuthToken(ArgumentMatchers.any()))
                    .thenThrow(new ExpiredTokenException(EXPIRED_TOKEN));
            commentService.createComment(comment, authToken);
        } catch (ExpiredTokenException e) {
            assertNotNull(e);
        }
    }


    @Test
    void getComments() {
       assertNotNull(commentService.getComments(VIDEO_ID));

       try {
           commentService.getComments(getRandomString());
       } catch (NotFoundsException e) {
           assertNotNull(e);
       }
    }

    @Test
    void getCommentById() {
        assertNotNull(commentService.getCommentById(COMMENT_ID));
    }

    @Test
    void likeComment() throws Exception {
        String authToken = "Token";
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        assertNotNull(commentService.likeComment(COMMENT_ID, authToken));
    }

    @Test
    void dislikeComment() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        assertNotNull(commentService.dislikeComment(COMMENT_ID, "Token"));
    }


}
