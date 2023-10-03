package com.youtorial.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import com.youtorial.backend.BaseTest;
import com.youtorial.backend.model.Account.User;
import com.youtorial.backend.model.Video.Video;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VideoControllerTest extends BaseTest {

    @MockBean
    AuthService authService;
    @Autowired
    UserService userService;

    private static final String VIDEO_ENDPOINT = URL_PREFIX+"/video";

    @Test
    void findAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(VIDEO_ENDPOINT))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void findVideoKO() throws Exception {
        MvcResult resultFind = mockMvc.perform(
                MockMvcRequestBuilders.get(VIDEO_ENDPOINT+"/videoInfo/{id}", "1"))
                .andReturn();
        assertEquals(204, resultFind.getResponse().getStatus());
    }

    @Test
    void saveVideo() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        User user = userService.getUserById(USER_ID);

        Video video = new Video();
        video.setTitle(getRandomString());
        video.setDescription(getRandomString());
        video.setUploadDate(LocalDateTime.now());
        video.setUrl(getRandomString());
        video.setDuration(getRandomString());
        video.setAuthor(getRandomString());
        video.setCategory(new ArrayList<>());
        video.setTutorialId(getRandomString());
        video.setLocation(getRandomString());

        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post(VIDEO_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(video))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "token"))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        //findVideoById
        MvcResult findVideoResult = mockMvc.perform(MockMvcRequestBuilders.get(VIDEO_ENDPOINT+"/videoInfo/{id}", id))
                .andReturn();
        Assertions.assertEquals(200, findVideoResult.getResponse().getStatus());

        //updateVideo
        video.setId(id);
        video.setTitle(getRandomString());
        MvcResult updateResult =  mockMvc.perform(MockMvcRequestBuilders.put(VIDEO_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(video))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        Assertions.assertEquals(200, updateResult.getResponse().getStatus());

        //deleteVideo
        MvcResult deleteResult = mockMvc.perform(MockMvcRequestBuilders.delete(VIDEO_ENDPOINT+"/{id}", id))
                .andReturn();
        Assertions.assertEquals(200, deleteResult.getResponse().getStatus());


        //InvalidToken
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new InvalidTokenException("Invalid Token"));

        MvcResult invalidTokenResult = mockMvc.perform(
                        MockMvcRequestBuilders.post(VIDEO_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(video))
                                .accept(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, "token"))
                .andReturn();
        Assertions.assertEquals(403, invalidTokenResult.getResponse().getStatus());

    }

    @Test
    void saveVideoExpiredKO() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Video video = new Video();
        video.setTitle(getRandomString());
        video.setDescription(getRandomString());
        video.setUploadDate(LocalDateTime.now());
        video.setUrl(getRandomString());
        video.setDuration(getRandomString());
        video.setAuthor(getRandomString());
        video.setCategory(new ArrayList<>());
        video.setTutorialId(getRandomString());
        video.setLocation(getRandomString());
        //ExpiredToken
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new ExpiredTokenException("Expired Token"));

        MvcResult expiredTokenResult = mockMvc.perform(
                        MockMvcRequestBuilders.post(VIDEO_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(video))
                                .accept(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, "token"))
                .andReturn();
        Assertions.assertEquals(401, expiredTokenResult.getResponse().getStatus());
    }

    @Test
    void updateVideoKO() throws Exception {
        Video video = new Video();
        video.setId("id");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(VIDEO_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(video))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        Assertions.assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    void deleteVideoKO() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(VIDEO_ENDPOINT+"/{id}", "id"))
                .andReturn();
        Assertions.assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    void query() throws Exception {
        String title  = "Olcx1aefoeQT";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(VIDEO_ENDPOINT+"/query/{queryString}", title))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        MvcResult emptyResult = mockMvc.perform(MockMvcRequestBuilders.get(VIDEO_ENDPOINT+"/query/{queryString}", "__"))
                .andReturn();
        Assertions.assertEquals(204, emptyResult.getResponse().getStatus());
    }

    @Test
    void queryByCategory() throws Exception {
        List<String> categoryList = new ArrayList<>();
        categoryList.add("category1");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(VIDEO_ENDPOINT+"/queryByCategory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryList))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        List<String> emptyCategoryList = new ArrayList<>();
        emptyCategoryList.add(getRandomString());
        MvcResult emptyResult = mockMvc.perform(MockMvcRequestBuilders.post(VIDEO_ENDPOINT+"/queryByCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyCategoryList))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        Assertions.assertEquals(204, emptyResult.getResponse().getStatus());
    }

    @Test
    void getVideoByUser() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(VIDEO_ENDPOINT+"/videoByUser/{userId}", USER_ID))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());
    }




}