package com.youtorial.backend.controller;

import com.youtorial.backend.BaseTest;
import com.youtorial.backend.model.Account.User;
import com.youtorial.backend.model.utils.Notes;
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

import static org.mockito.Mockito.when;

class InteractionControllerTest extends BaseTest {

    private static final String INTERACTION_ENDPOINT = URL_PREFIX+"/interaction";

    @MockBean
    AuthService authService;
    @Autowired
    UserService userService;

    @Test
    void rateVideo() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                        INTERACTION_ENDPOINT + "/rateVideo/{videoId}/{ratingValue}",
                        VIDEO_ID, 2)
                .header(AUTHORIZATION, "Token")).andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new InvalidTokenException("Invalid Token"));
        MvcResult invalidTokenResult = mockMvc.perform(MockMvcRequestBuilders.get(
                        INTERACTION_ENDPOINT + "/rateVideo/{videoId}/{ratingValue}",
                        VIDEO_ID, 2)
                .header(AUTHORIZATION, "Token")).andReturn();
        Assertions.assertEquals(403, invalidTokenResult.getResponse().getStatus());
    }

    @Test
    void rateVideoExpiredKO() throws Exception {
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new ExpiredTokenException("Exipred Token"));
        MvcResult invalidTokenResult = mockMvc.perform(MockMvcRequestBuilders.get(
                        INTERACTION_ENDPOINT + "/rateVideo/{videoId}/{ratingValue}",
                        VIDEO_ID, 2)
                .header(AUTHORIZATION, "Token")).andReturn();
        Assertions.assertEquals(401, invalidTokenResult.getResponse().getStatus());
    }

    @Test
    void getRating() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT + "/getRating/{videoId}",
                                VIDEO_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new InvalidTokenException("Invalid Token"));
        MvcResult expiredTokenResult = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT + "/getRating/{videoId}",
                                VIDEO_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(403, expiredTokenResult.getResponse().getStatus());
    }

    @Test
    void getRatingExpiredKO() throws Exception {
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new ExpiredTokenException("Expired Token"));
        MvcResult expiredTokenResult = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT + "/getRating/{videoId}",
                                VIDEO_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(401, expiredTokenResult.getResponse().getStatus());
    }

    @Test
    void saveVideo() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT+"/saveVideo/{videoId}/{savedBoolean}",
                                VIDEO_ID, true)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new InvalidTokenException("Invalid Token"));
        MvcResult invalidTokenResult = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT+"/saveVideo/{videoId}/{savedBoolean}",
                                VIDEO_ID, true)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(403, invalidTokenResult.getResponse().getStatus());
    }

    @Test
    void saveVideoExpired() throws Exception {
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new ExpiredTokenException("Expired Token"));
        MvcResult expiredTokenResult = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT+"/saveVideo/{videoId}/{savedBoolean}",
                                VIDEO_ID, true)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(401, expiredTokenResult.getResponse().getStatus());
    }

    @Test
    void checkStep() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                INTERACTION_ENDPOINT+"/checkStep/{videoId}/{stepId}/{checkBoolean}",
                VIDEO_ID, STEP_ID, true)
                .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new InvalidTokenException("Invalid Token"));
        MvcResult invalidTokenResult = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT+"/checkStep/{videoId}/{stepId}/{checkBoolean}",
                                VIDEO_ID, STEP_ID, true)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(403, invalidTokenResult.getResponse().getStatus());
    }

    @Test
    void checkStepExpiredKO() throws Exception {
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new ExpiredTokenException("Invalid Token"));
        MvcResult expiredTokenResult = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT+"/checkStep/{videoId}/{stepId}/{checkBoolean}",
                                VIDEO_ID, STEP_ID, true)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(401, expiredTokenResult.getResponse().getStatus());
    }

    @Test
    void getCheckedStep() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                INTERACTION_ENDPOINT+"/getCheckedStep/{videoId}/{stepId}",
                VIDEO_ID, STEP_ID)
                .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new InvalidTokenException("Invalid token"));
        MvcResult invalidResult = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT+"/getCheckedStep/{videoId}/{stepId}",
                                VIDEO_ID, STEP_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(403, invalidResult.getResponse().getStatus());
    }

    @Test
    void getCheckedStepExpiredKO() throws Exception {
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new ExpiredTokenException("Expired token"));
        MvcResult expiredResult = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT+"/getCheckedStep/{videoId}/{stepId}",
                                VIDEO_ID, STEP_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(401, expiredResult.getResponse().getStatus());
    }

    @Test
    void saveNotes() throws Exception {
        Notes notes = new Notes();
        notes.setNote(getRandomString());
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(
                INTERACTION_ENDPOINT+"/saveNotes/{videoId}/{stepId}",
                VIDEO_ID, STEP_ID)
                .header(AUTHORIZATION, "Token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notes)))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new InvalidTokenException("Invalid Token"));
        MvcResult invalidResult = mockMvc.perform(MockMvcRequestBuilders.post(
                                INTERACTION_ENDPOINT+"/saveNotes/{videoId}/{stepId}",
                                VIDEO_ID, STEP_ID)
                        .header(AUTHORIZATION, "Token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notes)))
                .andReturn();
        Assertions.assertEquals(403, invalidResult.getResponse().getStatus());
    }

    @Test
    void saveNotesExpiredKO() throws Exception {
        Notes notes = new Notes();
        notes.setNote(getRandomString());
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new ExpiredTokenException("Invalid Token"));
        MvcResult invalidResult = mockMvc.perform(MockMvcRequestBuilders.post(
                                INTERACTION_ENDPOINT+"/saveNotes/{videoId}/{stepId}",
                                VIDEO_ID, STEP_ID)
                        .header(AUTHORIZATION, "Token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notes)))
                .andReturn();
        Assertions.assertEquals(401, invalidResult.getResponse().getStatus());
    }

    @Test
    void getSavedNotes() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                INTERACTION_ENDPOINT+"/getSavedNotes/{videoId}/{stepId}",
                VIDEO_ID, STEP_ID)
                .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new InvalidTokenException("Invalid Token"));
        MvcResult invalidResult = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT+"/getSavedNotes/{videoId}/{stepId}",
                                VIDEO_ID, STEP_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(403, invalidResult.getResponse().getStatus());
    }

    @Test
    void getSavedNotesExpiredKO() throws Exception {
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new ExpiredTokenException("Expired Token"));
        MvcResult invalidResult = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT+"/getSavedNotes/{videoId}/{stepId}",
                                VIDEO_ID, STEP_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(401, invalidResult.getResponse().getStatus());
    }

    @Test
    void getCompletionPercentage() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                INTERACTION_ENDPOINT+"/getCompletionPercentage/{videoId}",
                VIDEO_ID)
                .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new InvalidTokenException("Invalid Exception"));
        MvcResult invalidResult = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT+"/getCompletionPercentage/{videoId}",
                                VIDEO_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(403, invalidResult.getResponse().getStatus());
    }

    @Test
    void getCompletionPercentageExpiredKO() throws Exception {
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new ExpiredTokenException("Expired Exception"));
        MvcResult invalidResult = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT+"/getCompletionPercentage/{videoId}",
                                VIDEO_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(401, invalidResult.getResponse().getStatus());
    }

    @Test
    void subscribe() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                INTERACTION_ENDPOINT+"/subscribe/{authorId}",
                USER_ID)
                .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new InvalidTokenException("Invalid Token"));
        MvcResult invalidResult = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT+"/subscribe/{authorId}",
                                USER_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(403, invalidResult.getResponse().getStatus());
    }

    @Test
    void subscribeExpiredKO() throws Exception {
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new ExpiredTokenException("Expired Token"));
        MvcResult invalidResult = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT+"/subscribe/{authorId}",
                                USER_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(401, invalidResult.getResponse().getStatus());
    }

    @Test
    void unsubscribe() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                INTERACTION_ENDPOINT+"/unsubscribe/{authorId}",
                USER_ID)
                .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new InvalidTokenException("Invalid Token"));
        MvcResult invalidResult = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT+"/unsubscribe/{authorId}",
                                USER_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(403, invalidResult.getResponse().getStatus());
    }

    @Test
    void unsubscribeExpiredKO() throws Exception {
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new ExpiredTokenException("Expired Token"));
        MvcResult invalidResult = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT+"/unsubscribe/{authorId}",
                                USER_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(401, invalidResult.getResponse().getStatus());
    }

    @Test
    void isSubbed() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                INTERACTION_ENDPOINT+"/isSubscribed/{authorId}",
                USER_ID)
                .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new InvalidTokenException("Invalid Token"));
        MvcResult invalidResult = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT+"/isSubscribed/{authorId}",
                                USER_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(403, invalidResult.getResponse().getStatus());
    }


    @Test
    void isSubbedExpiredKO() throws Exception {
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new ExpiredTokenException("Expired Token"));
        MvcResult invalidResult = mockMvc.perform(MockMvcRequestBuilders.get(
                                INTERACTION_ENDPOINT+"/isSubscribed/{authorId}",
                                USER_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(401, invalidResult.getResponse().getStatus());
    }



}
