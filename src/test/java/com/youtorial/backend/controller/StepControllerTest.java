package com.youtorial.backend.controller;

import com.jayway.jsonpath.JsonPath;
import com.youtorial.backend.BaseTest;
import com.youtorial.backend.model.Step.StepDtoInput;
import com.youtorial.backend.service.AuthService;
import com.youtorial.backend.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class StepControllerTest extends BaseTest {


    private static final String STEP_ENDPOINT = URL_PREFIX+"/step";

    @Test
    void findAll() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(STEP_ENDPOINT))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void findStep() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(STEP_ENDPOINT+"/{id}", STEP_ID))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        MvcResult emptyResult = mockMvc.perform(MockMvcRequestBuilders.get(STEP_ENDPOINT+"/{id}", "step id"))
                .andReturn();
        Assertions.assertEquals(204, emptyResult.getResponse().getStatus());
    }

    @Test
    void save() throws Exception {
        StepDtoInput input = new StepDtoInput();
        input.setTitle(getRandomString());
        input.setVideoId(VIDEO_ID);
        input.setEndTime(getRandomString());
        input.setStartTime(getRandomString());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(STEP_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        //Update
        input.setId(id);
        MvcResult updateResult = mockMvc.perform(MockMvcRequestBuilders.put(STEP_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andReturn();
        Assertions.assertEquals(200, updateResult.getResponse().getStatus());


        //Delete
        MvcResult deleteResult = mockMvc.perform(MockMvcRequestBuilders.delete(STEP_ENDPOINT+"/{id}", id))
                .andReturn();
        Assertions.assertEquals(200, deleteResult.getResponse().getStatus());
    }

    @Test
    void updateKOResult() throws Exception {
        StepDtoInput input = new StepDtoInput();
        //UpdateNotFoundKO
        input.setId("Id");
        MvcResult updateKOResult = mockMvc.perform(MockMvcRequestBuilders.put(STEP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andReturn();
        Assertions.assertEquals(404, updateKOResult.getResponse().getStatus());
    }

    @Test
    void deleteKOResult() throws Exception {
        MvcResult deleteResult = mockMvc.perform(MockMvcRequestBuilders.delete(STEP_ENDPOINT+"/{id}", "stepId"))
                .andReturn();
        Assertions.assertEquals(404, deleteResult.getResponse().getStatus());
    }
}
