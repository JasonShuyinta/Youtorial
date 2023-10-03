package com.youtorial.backend.controller;

import com.jayway.jsonpath.JsonPath;
import com.youtorial.backend.BaseTest;
import com.youtorial.backend.model.Account.User;
import com.youtorial.backend.model.Step.StepDtoInput;
import com.youtorial.backend.model.Tutorial.TutorialDtoInput;
import com.youtorial.backend.model.utils.PayloadDeleteStep;
import com.youtorial.backend.service.AuthService;
import com.youtorial.backend.service.UserService;
import com.youtorial.backend.utils.exception.ExpiredTokenException;
import com.youtorial.backend.utils.exception.InvalidTokenException;
import com.youtorial.backend.utils.exception.NotFoundsException;
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

class TutorialControllerTest extends BaseTest {

    @MockBean
    AuthService authService;
    @Autowired
    UserService userService;

    private static final String TUTORIAL_ENDPOINT = URL_PREFIX+"/tutorial";

    @Test
    void saveStep() throws Exception {
        StepDtoInput stepDtoInput = new StepDtoInput();
        stepDtoInput.setVideoId(VIDEO_ID);
        stepDtoInput.setTitle(getRandomString());
        stepDtoInput.setEndTime(getRandomString());

        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(TUTORIAL_ENDPOINT + "/saveStep")
                        .header(AUTHORIZATION, "Token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stepDtoInput)))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        String stepIdOutput = JsonPath.read(result.getResponse().getContentAsString(), "$.steps[0].id");

        //Update
        stepDtoInput.setTitle(getRandomString());
        MvcResult updateResult = mockMvc.perform(MockMvcRequestBuilders.put(TUTORIAL_ENDPOINT + "/updateStep/{stepId}", stepIdOutput)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stepDtoInput)))
                .andReturn();
        Assertions.assertEquals(200, updateResult.getResponse().getStatus());

        String stepId = JsonPath.read(updateResult.getResponse().getContentAsString(), "$.id");

        //UpdateNotFoundKO
        MvcResult updateKOResult = mockMvc.perform(MockMvcRequestBuilders.put(TUTORIAL_ENDPOINT + "/updateStep/{stepId}", getRandomString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stepDtoInput)))
                .andReturn();
        Assertions.assertEquals(404, updateKOResult.getResponse().getStatus());

        //DeleteStep
        PayloadDeleteStep payloadDeleteStep = new PayloadDeleteStep();
        payloadDeleteStep.setStepId(stepId);
        payloadDeleteStep.setTutorialId(id);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult deleteResult = mockMvc.perform(MockMvcRequestBuilders.post(TUTORIAL_ENDPOINT + "/deleteStep")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Token")
                        .content(objectMapper.writeValueAsString(payloadDeleteStep)))
                .andReturn();
        Assertions.assertEquals(200, deleteResult.getResponse().getStatus());

        //Delete Not Found
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new NotFoundsException("Not Found error"));
        MvcResult deleteResultKO = mockMvc.perform(MockMvcRequestBuilders.post(TUTORIAL_ENDPOINT + "/deleteStep")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Token")
                        .content(objectMapper.writeValueAsString(payloadDeleteStep)))
                .andReturn();
        Assertions.assertEquals(404, deleteResultKO.getResponse().getStatus());

    }

    @Test
    void saveStepInvalidKO() throws Exception {
        StepDtoInput stepDtoInput = new StepDtoInput();
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new InvalidTokenException("Invalid token"));
        MvcResult invalidResult = mockMvc.perform(MockMvcRequestBuilders.post(TUTORIAL_ENDPOINT + "/saveStep")
                        .header(AUTHORIZATION, "Token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stepDtoInput)))
                .andReturn();
        Assertions.assertEquals(403, invalidResult.getResponse().getStatus());
    }

    @Test
    void saveStepExpiredKO() throws Exception {
        StepDtoInput stepDtoInput = new StepDtoInput();
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new ExpiredTokenException("Expired token"));
        MvcResult expiredResult = mockMvc.perform(MockMvcRequestBuilders.post(TUTORIAL_ENDPOINT + "/saveStep")
                        .header(AUTHORIZATION, "Token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stepDtoInput)))
                .andReturn();
        Assertions.assertEquals(401, expiredResult.getResponse().getStatus());
    }

    @Test
    void getStepsNumber() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(TUTORIAL_ENDPOINT + "/getStepsNumber/{videoId}", "Qda6mqGhLsiA"))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void getStepsNumberNotFoundKO() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(TUTORIAL_ENDPOINT + "/getStepsNumber/{videoId}", getRandomString()))
                .andReturn();
        Assertions.assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    void getStepsNumberTutorialId() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(TUTORIAL_ENDPOINT + "/getStepsNumberTutorialId/{tutorialId}", TUTORIAL_ID))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void getStepsNumberTutorialIdNotFoundKO() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(TUTORIAL_ENDPOINT + "/getStepsNumberTutorialId/{tutorialId}", TUTORIAL_ID))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        MvcResult notFoundResult = mockMvc.perform(MockMvcRequestBuilders.get(TUTORIAL_ENDPOINT + "/getStepsNumberTutorialId/{tutorialId}", "TutorialID"))
                .andReturn();
        Assertions.assertEquals(404, notFoundResult.getResponse().getStatus());
    }

    @Test
    void getSelectedTutorial() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(TUTORIAL_ENDPOINT + "/getSelectedTutorial/{videoId}", "Qda6mqGhLsiA"))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        //Empty
        MvcResult emptyResult = mockMvc.perform(MockMvcRequestBuilders.get(TUTORIAL_ENDPOINT + "/getSelectedTutorial/{videoId}", "videoId"))
                .andReturn();
        Assertions.assertEquals(204, emptyResult.getResponse().getStatus());
    }

    @Test
    void getLastTutorialEndTime() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(TUTORIAL_ENDPOINT + "/getLastTutorialEndTime/{videoId}", "Qda6mqGhLsiA"))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        MvcResult notFoundResult = mockMvc.perform(MockMvcRequestBuilders.get(TUTORIAL_ENDPOINT + "/getLastTutorialEndTime/{videoId}", "videoId"))
                .andReturn();
        Assertions.assertEquals(404, notFoundResult.getResponse().getStatus());
    }

    @Test
    void checkDifferences() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(TUTORIAL_ENDPOINT + "/checkDifferences/{tutorialId}", TUTORIAL_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        //Assertions.assertEquals(200, result.getResponse().getStatus());

        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult notFoundResult = mockMvc.perform(MockMvcRequestBuilders.get(TUTORIAL_ENDPOINT + "/checkDifferences/{tutorialId}", "Some Tutorial")
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(404, notFoundResult.getResponse().getStatus());
    }

    @Test
    void checkDifferencesInvalidKO() throws Exception {
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new InvalidTokenException("Invalid token"));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(TUTORIAL_ENDPOINT + "/checkDifferences/{tutorialId}", TUTORIAL_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(403, result.getResponse().getStatus());
    }

    @Test
    void checkDifferencesExpiredKO() throws Exception {
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenThrow(new ExpiredTokenException("Expired token"));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(TUTORIAL_ENDPOINT + "/checkDifferences/{tutorialId}", TUTORIAL_ID)
                        .header(AUTHORIZATION, "Token"))
                .andReturn();
        Assertions.assertEquals(401, result.getResponse().getStatus());
    }

    @Test
    void findAll() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(TUTORIAL_ENDPOINT))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void findTutorial() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(TUTORIAL_ENDPOINT + "/{id}", TUTORIAL_ID))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        MvcResult emptyResult = mockMvc.perform(MockMvcRequestBuilders.get(TUTORIAL_ENDPOINT + "/{id}", "TutorialId"))
                .andReturn();
        Assertions.assertEquals(204, emptyResult.getResponse().getStatus());
    }

    @Test
    void save() throws Exception {
        TutorialDtoInput input = new TutorialDtoInput();
        input.setVideoId(VIDEO_ID);
        input.setSteps(new ArrayList<>());
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(TUTORIAL_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Token")
                .content(objectMapper.writeValueAsString(input))).
                andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        input.setId(id);
        MvcResult updateResult = mockMvc.perform(MockMvcRequestBuilders.put(TUTORIAL_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andReturn();
        Assertions.assertEquals(200, updateResult.getResponse().getStatus());

        //Delete
        MvcResult deleteResult = mockMvc.perform(MockMvcRequestBuilders.delete(TUTORIAL_ENDPOINT+"/{id}", id))
                .andReturn();
        Assertions.assertEquals(200, deleteResult.getResponse().getStatus());
    }
}
