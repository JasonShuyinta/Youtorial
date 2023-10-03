package com.youtorial.backend.controller;

import com.jayway.jsonpath.JsonPath;
import com.youtorial.backend.BaseTest;
import com.youtorial.backend.model.Account.User;
import com.youtorial.backend.model.Tutorial.TutorialDtoInput;
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

class PublicControllerTest extends BaseTest {

    private static final String PUBLIC_ENDPOINT = URL_PREFIX+"/public";

    @MockBean
    AuthService authService;
    @Autowired
    UserService userService;

    @Test
    void getTutorialsAuthor() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(PUBLIC_ENDPOINT+"/getTutorialAuthor/{videoId}", VIDEO_ID))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        MvcResult emptyResult = mockMvc.perform(MockMvcRequestBuilders.get(PUBLIC_ENDPOINT+"/getTutorialAuthor/{videoId}", "videoId"))
                .andReturn();
        Assertions.assertEquals(404, emptyResult.getResponse().getStatus());
    }

    /*
    @Test
    void publish() throws Exception {
        TutorialDtoInput input = new TutorialDtoInput();
        input.setId(TUTORIAL_ID);
        input.setAuthor(USER_ID);
        input.setVideoId(VIDEO_ID);
        input.setSteps(new ArrayList<>());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(PUBLIC_ENDPOINT+"/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        //Delete
        MvcResult deleteResult = mockMvc.perform(MockMvcRequestBuilders.delete(PUBLIC_ENDPOINT+"/{publicTutorialId}", id ))
                .andReturn();
        Assertions.assertEquals(200, deleteResult.getResponse().getStatus());
    }
*/
    @Test
    void deleteNotFoundKO() throws Exception {
        MvcResult deleteResult = mockMvc.perform(MockMvcRequestBuilders.delete(PUBLIC_ENDPOINT+"/{publicTutorialId}", getRandomString() ))
                .andReturn();
        Assertions.assertEquals(404, deleteResult.getResponse().getStatus());
    }

    @Test
    void publishNotFoundKO() throws Exception {
        TutorialDtoInput input = new TutorialDtoInput();
        input.setId(getRandomString());
        input.setVideoId(getRandomString());
        MvcResult notFoundResult = mockMvc.perform(MockMvcRequestBuilders.post(PUBLIC_ENDPOINT+"/publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andReturn();
        Assertions.assertEquals(404, notFoundResult.getResponse().getStatus());
    }

    @Test
    void isAuthor() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(PUBLIC_ENDPOINT+"/isAuthor/{videoId}/{userId}",
                        VIDEO_ID, USER_ID))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        MvcResult notFoundResult = mockMvc.perform(MockMvcRequestBuilders.get(PUBLIC_ENDPOINT+"/isAuthor/{videoId}/{userId}",
                        VIDEO_ID, getRandomString()))
                .andReturn();
        Assertions.assertEquals(404, notFoundResult.getResponse().getStatus());
    }
/*
    @Test
    void getPublicTutorial() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(PUBLIC_ENDPOINT+"/getPublicTutorial/{videoId}",
                        VIDEO_ID))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        MvcResult emptyResult = mockMvc.perform(MockMvcRequestBuilders.get(PUBLIC_ENDPOINT+"/getPublicTutorial/{videoId}",
                        getRandomString()))
                .andReturn();
        Assertions.assertEquals(204, emptyResult.getResponse().getStatus());
    }
*/
    @Test
    void getTutorialRating() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                PUBLIC_ENDPOINT+"/getTutorialRating/{videoId}/{authorId}",
                "62921d98f1c1ce388e1bf3e3", USER_ID )).andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        MvcResult notFoundResult = mockMvc.perform(MockMvcRequestBuilders.get(
                PUBLIC_ENDPOINT+"/getTutorialRating/{videoId}/{authorId}",
                "62921d98f1c1ce388e1bf3edd", USER_ID )).andReturn();
        Assertions.assertEquals(404, notFoundResult.getResponse().getStatus());
    }

    @Test
    void getUserTutorials() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                PUBLIC_ENDPOINT+"/getUserTutorials")
                        .header(AUTHORIZATION, "Token")).andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new InvalidTokenException("Invalid token"));
        MvcResult invalidTokenResult = mockMvc.perform(MockMvcRequestBuilders.get(
                        PUBLIC_ENDPOINT+"/getUserTutorials")
                .header(AUTHORIZATION, "Token")).andReturn();
        Assertions.assertEquals(403, invalidTokenResult.getResponse().getStatus());
    }

    @Test
    void getUserTutorialsExpiredKO() throws Exception {
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new ExpiredTokenException("Expired token"));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                        PUBLIC_ENDPOINT+"/getUserTutorials")
                .header(AUTHORIZATION, "Token")).andReturn();
        Assertions.assertEquals(401, result.getResponse().getStatus());
    }

    @Test
    void getAuthorByTutorialId() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                PUBLIC_ENDPOINT+"/getAuthorByTutorialId/{tutorialId}",
                PUBLIC_ID
        )).andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        MvcResult notFoundResult = mockMvc.perform(MockMvcRequestBuilders.get(
                PUBLIC_ENDPOINT+"/getAuthorByTutorialId/{tutorialId}",
                "publicId"
        )).andReturn();
        Assertions.assertEquals(404, notFoundResult.getResponse().getStatus());
    }

}
