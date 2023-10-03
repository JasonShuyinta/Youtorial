package com.youtorial.backend.service;

import com.youtorial.backend.BaseTest;
import com.youtorial.backend.model.Account.User;
import com.youtorial.backend.model.utils.Notes;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class InteractionServiceTest extends BaseTest {

    @Autowired
    UserService userService;
    @Autowired
    InteractionService interactionService;
    @MockBean
    AuthService authService;

    @Test
    void rateVideo() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        try {
            interactionService.rateVideo(VIDEO_ID, 3, "Token");
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    void getRating() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        try {
            interactionService.getRating(VIDEO_ID, "Token");
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    void saveVideo() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        try {
            interactionService.saveVideo(VIDEO_ID, true, "Token");
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    void checkStep() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        try {
            interactionService.checkStep(VIDEO_ID, STEP_ID, true, "Token");
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    void getCheckedStep() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        try {
            interactionService.getCheckedStep(VIDEO_ID, STEP_ID, "Token");
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    void saveNotes() throws Exception {
        Notes notes = new Notes();
        notes.setNote("notes");
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        try {
            interactionService.saveNotes(VIDEO_ID, STEP_ID, "Token", notes);
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    void getNotes() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        try {
            interactionService.getNotes(VIDEO_ID, STEP_ID, "Token");
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    void getCompletionPercentage() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        try {
            interactionService.getCompletionPercentage(VIDEO_ID, "Token");
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    void subscribeChannel() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        try {
            interactionService.subscribeChannel(USER_ID, "Token");
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    void unsubscribeChannel() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        try {
            interactionService.unsubscribeChannel(USER_ID, "Token");
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    void isSubscribed() throws Exception {
        User user = userService.getUserById(USER_ID);
        when(authService.getUserByAuthToken(ArgumentMatchers.any())).thenReturn(user);
        try {
            interactionService.isSubscribed(USER_ID, "Token");
        } catch (Exception e) {
            assertNull(e);
        }
    }
}
