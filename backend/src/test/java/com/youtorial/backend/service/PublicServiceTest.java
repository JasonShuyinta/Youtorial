package com.youtorial.backend.service;

import com.youtorial.backend.BaseTest;
import com.youtorial.backend.model.Account.User;
import com.youtorial.backend.model.Tutorial.Tutorial;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class PublicServiceTest extends BaseTest {

    @Autowired
    PublicService publicService;
    @Autowired
    UserService userService;
    @MockBean
    AuthService authService;

    @Test
    void getTutorialAuthor() throws Exception {
        try {
            publicService.getTutorialAuthor(VIDEO_ID);
        } catch (Exception e) {
            assertNull(e);
        }
    }
/*
    @Test
    void publishTutorial() throws Exception {
        try {
            Tutorial tutorial = new Tutorial();
            tutorial.setId(TUTORIAL_ID);
            tutorial.setVideoId(VIDEO_ID);
            publicService.publishTutorial(tutorial);
        } catch (Exception e) {
            assertNull(e);
        }
    }
*/
    @Test
    void isAuthor() throws Exception {
        try {
            publicService.isAuthor(VIDEO_ID, USER_ID);
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    void getPublicByAuthor() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        try {
            publicService.getPublicByAuthor("Token");
        } catch (Exception e) {
            assertNull(e);
        }
    }


    @Test
    void getAuthor() throws Exception {
        try {
            publicService.getAuthor(PUBLIC_ID);
        }catch (Exception e) {
            assertNull(e);
        }
    }
}
