package com.youtorial.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class AppConfig {

    @Value("${spring.app.verificationUrl}")
    public String verificationUrl;

    @Value("${spring.app.secretKey}")
    public String secretKey;

    @Value("${spring.app.loginUrl}")
    public String loginUrl;

    public String getVerificationUrl() {
        return verificationUrl;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getLoginUrl() { return loginUrl; }
}
