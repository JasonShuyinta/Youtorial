package com.youtorial.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CoreTestSpringConfiguration.class})
public abstract class BaseTest {

    public static final String INVALID_TOKEN = "Invalid Token";
    public static final String EXPIRED_TOKEN = "Expired Token";
    public static final String NOT_FOUND = "Not Found";
    public static final String AUTHORIZATION = "Authorization";
    public static final String USER_ID = "6290f87b3e158f73466c806c";
    public static final String VIDEO_ID = "62911b33e475bb473d47d3ee";
    public static final String COMMENT_ID = "6293b3a908c78e6639b972ea";
    public static final String STEP_ID = "6293cef054d30541ed691509";
    public static final String TUTORIAL_ID = "6293ce3c03b5515f8b12c73c";
    public static final String PUBLIC_ID = "629502e3732bdf19941af932";
    public static final String URL_PREFIX = "/api/v1";

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected WebApplicationContext wac;

    public MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    public String getRandomString() {
        String asciiUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String asciiLowerCase = asciiUpperCase.toLowerCase();
        String digits = "1234567890";
        String asciiChars = asciiUpperCase + asciiLowerCase + digits;
        return generateRandomString(asciiChars);
    }

    private static String generateRandomString(String seedChars) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        Random rand = new Random();
        while (i < 12) {
            sb.append(seedChars.charAt(rand.nextInt(seedChars.length())));
            i++;
        }
        return sb.toString();
    }
}
