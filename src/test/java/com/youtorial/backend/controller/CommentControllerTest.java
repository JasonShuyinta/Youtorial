package com.youtorial.backend.controller;

import com.jayway.jsonpath.JsonPath;
import com.youtorial.backend.BaseTest;
import com.youtorial.backend.model.Account.User;
import com.youtorial.backend.model.Comment.Comment;
import com.youtorial.backend.service.AuthService;
import com.youtorial.backend.service.UserService;
import com.youtorial.backend.utils.exception.ExpiredTokenException;
import com.youtorial.backend.utils.exception.InvalidTokenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

class CommentControllerTest extends BaseTest {

    @MockBean
    AuthService authService;
    @Autowired
    UserService userService;

    private static final String COMMENT_ENDPOINT = URL_PREFIX+"/comment";

    @Test
    void saveComment() throws Exception {
        Comment input = new Comment();
        input.setAuthor(getRandomString());
        input.setVideoId(getRandomString());
        input.setLikes(new ArrayList<>());
        input.setDislikes(new ArrayList<>());
        input.setResponseTo(getRandomString());
        input.setCommentText(getRandomString());
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(COMMENT_ENDPOINT + "/createComment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .header(AUTHORIZATION, "1"))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        //delete token
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult deleteResult = mockMvc.perform(MockMvcRequestBuilders.delete(COMMENT_ENDPOINT+"/{commentId}", id)
                .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(200, deleteResult.getResponse().getStatus());

        //InvalidTokenException
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new InvalidTokenException("Invalid Token"));
        MvcResult invalidTokenResult = mockMvc.perform(MockMvcRequestBuilders.post(COMMENT_ENDPOINT + "/createComment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .header(AUTHORIZATION, "1"))
                .andReturn();
        Assertions.assertEquals(403, invalidTokenResult.getResponse().getStatus());
    }

    @Test
    void saveCommentExpiredKO() throws Exception {
        Comment input = new Comment();
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new ExpiredTokenException("Invalid Token"));
        MvcResult expiredTokenResult = mockMvc.perform(MockMvcRequestBuilders.post(COMMENT_ENDPOINT + "/createComment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .header(AUTHORIZATION, "1"))
                .andReturn();
        Assertions.assertEquals(401, expiredTokenResult.getResponse().getStatus());
    }

    @Test
    void getComments() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(COMMENT_ENDPOINT+"/getComments/{videoId}", VIDEO_ID))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());


        MvcResult emptyResult = mockMvc.perform(MockMvcRequestBuilders.get(COMMENT_ENDPOINT + "/getComments/{videoId}", getRandomString())
        ).andReturn();
        Assertions.assertEquals(204, emptyResult.getResponse().getStatus());
    }

    @Test
    void getSingleComment() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                COMMENT_ENDPOINT+"/getSingleComment/{commentId}", COMMENT_ID))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void likeComment() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(COMMENT_ENDPOINT+"/likeComment/{commentId}", COMMENT_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        //InvalidToken
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new InvalidTokenException("Invalid token"));
        MvcResult invalidResult = mockMvc.perform(MockMvcRequestBuilders.get(COMMENT_ENDPOINT+"/likeComment/{commentId}", COMMENT_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(403, invalidResult.getResponse().getStatus());
    }

    @Test
    void likeCommentExpiredKO() throws Exception {
        //ExpiredToken
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new ExpiredTokenException("expired token"));
        MvcResult invalidResult = mockMvc.perform(MockMvcRequestBuilders.get(COMMENT_ENDPOINT+"/likeComment/{commentId}", COMMENT_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(401, invalidResult.getResponse().getStatus());
    }

    @Test
    void dislikeComment() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(COMMENT_ENDPOINT+"/dislikeComment/{commentId}", COMMENT_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());


        //InvalidToken
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new InvalidTokenException("Invalid Token"));
        MvcResult invalidResult = mockMvc.perform(MockMvcRequestBuilders.get(COMMENT_ENDPOINT+"/dislikeComment/{commentId}", COMMENT_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(403, invalidResult.getResponse().getStatus());

    }

    @Test
    void dislikeCommentExpiredKO() throws Exception {
        //ExpiredToken
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new ExpiredTokenException("expiredResult Token"));
        MvcResult expiredResult = mockMvc.perform(MockMvcRequestBuilders.get(COMMENT_ENDPOINT+"/dislikeComment/{commentId}", COMMENT_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(401, expiredResult.getResponse().getStatus());

    }
}
