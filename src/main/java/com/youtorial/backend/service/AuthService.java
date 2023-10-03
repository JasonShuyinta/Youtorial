package com.youtorial.backend.service;

import com.google.gson.Gson;
import com.youtorial.backend.AppConfig;
import com.youtorial.backend.model.Account.User;
import com.youtorial.backend.model.utils.JWTObject;
import com.youtorial.backend.utils.exception.ExpiredTokenException;
import com.youtorial.backend.utils.exception.InvalidTokenException;
import com.youtorial.backend.utils.exception.NotFoundsException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

import static com.youtorial.backend.utils.Constants.*;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Service
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AppConfig appConfig;

    public User getUserByAuthToken(String authToken) throws InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            SignatureAlgorithm sa = HS256;
            SecretKeySpec secretKeySpec = new SecretKeySpec(appConfig.getSecretKey().getBytes(), sa.getJcaName());
            Base64.Decoder decoder = Base64.getUrlDecoder();
            if (authToken != null && authToken.startsWith("Bearer ")) {
                String[] tokens = authToken.split(" ");
                String[] access = tokens[1].split("\\.");
                String tokenWithoutSignature = access[0] + "." + access[1];
                DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);
                if (!validator.isValid(tokenWithoutSignature, access[2])) {
                    throw new InvalidTokenException("Could not verify token integrity");
                } else {
                    Gson g = new Gson();
                    String accessPayload = new String(decoder.decode(access[1]));
                    JWTObject jwtObject = g.fromJson(accessPayload, JWTObject.class);
                    Date expirationDate = new Date(Long.parseLong(jwtObject.getExp()) * 1000);
                    Date today = new Date(System.currentTimeMillis());
                    if (today.compareTo(expirationDate) > 0) {
                        throw new ExpiredTokenException("Token is expired");
                    } else {
                        log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                        return userService.getUserById(jwtObject.getSub());
                    }
                }
            } else throw new InvalidTokenException("Invalid token");
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (InvalidTokenException e) {
            log.error(e.toString());
            throw new InvalidTokenException(e.getMessage());
        } catch (ExpiredTokenException ex) {
            log.error(ex.toString());
            throw new ExpiredTokenException(ex.getMessage());
        } catch (Exception e) {
            log.error(e + this.getClass().getSimpleName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }
}
