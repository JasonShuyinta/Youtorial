package com.youtorial.backend.controller;

import com.youtorial.backend.BaseTest;
import com.youtorial.backend.model.Account.UserDtoInput;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

 import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

 @Slf4j
class UserControllerTest extends BaseTest {
     private static final String ACCESS_TOKEN = "accessToken";
     private static final String USER_ENDPOINT = URL_PREFIX+"/user";

//    @Test
//    void findAllUsers() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get(USER_ENDPOINT))
//                .andExpect(status().is2xxSuccessful());
//    }

    @Test
    void findUserByIdKO() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_ENDPOINT+"/{id}", "someId"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteUserByIdKO() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_ENDPOINT+"/{id}", "someId"))
                .andExpect(status().is4xxClientError());
    }

//    @Test
//    void saveUserAndFindUserOK() throws Exception {
//        String username = getRandomString();
//        String email = getRandomString();
//        UserDtoInput input = new UserDtoInput();
//        input.setFirstName("Jason");
//        input.setUsername(username);
//        input.setLastName("Shuyinta");
//        input.setPassword("123456");
//        input.setEmail(email);
//        input.setImage("Image");
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(USER_ENDPOINT+"/signup")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(input))
//                .accept(MediaType.APPLICATION_JSON))
//                .andReturn();
//        Assertions.assertEquals(200, result.getResponse().getStatus());
//
//        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
//
//        //FindUserById
//        mockMvc.perform(MockMvcRequestBuilders.get(USER_ENDPOINT+"/{id}", id))
//                .andExpect(status().is2xxSuccessful());
//
//        //UpdateUserById
//        input.setId(id);
//        MvcResult updateResult = mockMvc.perform(MockMvcRequestBuilders.put(USER_ENDPOINT)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(input))
//                .accept(MediaType.APPLICATION_JSON))
//                        .andReturn();
//        Assertions.assertEquals(200, updateResult.getResponse().getStatus());
//
//        //UpdateUsernameKO
//        input.setId(id);
//        input.setUsername("jay");
//        MvcResult usernameTakenResult = mockMvc.perform(MockMvcRequestBuilders.put(USER_ENDPOINT)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(input))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andReturn();
//        Assertions.assertEquals(400, usernameTakenResult.getResponse().getStatus());
//
//
//        //UpdateUserNotFoundKO
//        input.setId("1");
//        MvcResult userNotFoundResult = mockMvc.perform(MockMvcRequestBuilders.put(USER_ENDPOINT)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(input))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andReturn();
//        Assertions.assertEquals(404, userNotFoundResult.getResponse().getStatus());
//
//        //DeleteUserById
//        mockMvc.perform(MockMvcRequestBuilders.delete(USER_ENDPOINT+"/{id}", id))
//                .andExpect(status().is2xxSuccessful());
//    }

    @Test
    void signUpConflictKO() throws Exception {
        UserDtoInput input = new UserDtoInput();
        input.setEmail("jason@shuyinta.com");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(USER_ENDPOINT+"/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        Assertions.assertEquals(409, result.getResponse().getStatus());
    }

    @Test
    void signUpUsernameKO() throws Exception {
        String email = getRandomString();
        UserDtoInput input = new UserDtoInput();
        input.setEmail(email);
        input.setUsername("jay");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(USER_ENDPOINT+"/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        Assertions.assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void loginFailedPassword() throws Exception {
        UserDtoInput input = new UserDtoInput();
        input.setEmail("jason@shuyinta.com");
        input.setPassword("1");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(USER_ENDPOINT+"/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        Assertions.assertEquals(401, result.getResponse().getStatus());
    }

    @Test
    void loginFailedEmail() throws Exception {
        UserDtoInput input = new UserDtoInput();
        input.setEmail("wrongemail.com");
        input.setPassword("1");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(USER_ENDPOINT+"/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        Assertions.assertEquals(404, result.getResponse().getStatus());
    }

//    @Test
//    void tokenTest() throws Exception {
//        UserDtoInput input = new UserDtoInput();
//        input.setEmail("jason@shuyinta.com");
//        input.setPassword("123456");
//        MvcResult getTokenResult = mockMvc.perform(MockMvcRequestBuilders.post(USER_ENDPOINT+"/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(input))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andReturn();
//        String token = "Bearer " + getTokenResult.getResponse().getHeader(ACCESS_TOKEN);
//
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(USER_ENDPOINT+"/getUserByToken")
//                .header(AUTHORIZATION, token))
//                .andReturn();
//
//        Assertions.assertEquals(200, result.getResponse().getStatus());
//    }

    @Test
     void invalidTokenTest() throws Exception {
        String token = "invalidToken";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(USER_ENDPOINT+"/getUserByToken")
                        .header(AUTHORIZATION, token))
                .andReturn();
        Assertions.assertEquals(205, result.getResponse().getStatus());

    }


}
