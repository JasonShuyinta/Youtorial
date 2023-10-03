package com.youtorial.backend.controller;

import com.youtorial.backend.model.Account.User;
import com.youtorial.backend.model.Account.UserConverter;
import com.youtorial.backend.model.Account.UserDtoInput;
import com.youtorial.backend.model.Account.UserDtoOutput;
import com.youtorial.backend.model.utils.FeedbackDto;
import com.youtorial.backend.model.utils.PasswordPayload;
import com.youtorial.backend.service.AuthService;
import com.youtorial.backend.service.UserService;
import com.youtorial.backend.utils.exception.BadRequestException;
import com.youtorial.backend.utils.exception.ExpiredTokenException;
import com.youtorial.backend.utils.exception.InvalidTokenException;
import com.youtorial.backend.utils.exception.NotFoundsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.youtorial.backend.utils.Constants.*;
import static org.springframework.http.ResponseEntity.*;

@Controller
@RequestMapping("/api/v1/user")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<UserDtoOutput>> findAll(@RequestHeader("Authorization") String token) {

        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            User userAuthorized = authService.getUserByAuthToken(token);
            if (userAuthorized == null) return notFound().build();
            else {
                List<User> users = userService.getUsers();
                if (users.isEmpty()) return noContent().build();
                else {
                    List<UserDtoOutput> userDtoOutputs = new ArrayList<>();
                    for (User user : users)
                        userDtoOutputs.add(userConverter.entityToDtoOutput(user));
                    log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                    return ok(userDtoOutputs);
                }
            }
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @Cacheable(cacheNames = "user", key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<UserDtoOutput> findUser(@PathVariable String id) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            User user = userService.getUserById(id);
            UserDtoOutput dtoOutput = userConverter.entityToDtoOutput(user);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName()  , this.getClass().getSimpleName());
            return ok(dtoOutput);
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDtoOutput> save(@RequestBody UserDtoInput input) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            User checkEmail = userService.getUserByEmail(input.getEmail());
            if (checkEmail == null) {
                User user = userService.getUserByUsername(input.getUsername());
                if (user == null) {
                    User userSaved = userService.save(userConverter.dtoInputToEntity(input));
                    UserDtoOutput output = userConverter.entityToDtoOutput(userSaved);
                    log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName()  , this.getClass().getSimpleName());
                    return ok(output);
                } else {
                    return badRequest().build();
                }
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody UserDtoInput input, @RequestHeader("Authorization") String token) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            User userAuthorized = authService.getUserByAuthToken(token);
            if (userAuthorized == null) return notFound().build();
            else {
                User user = userService.getUserById(input.getId());
                if (user == null) return notFound().build();
                else {
                    if (passwordEncoder.matches(input.getPassword(), user.getPassword())) {
                        userService.delete(input.getId());
                        log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName()  , this.getClass().getSimpleName());
                        return ok().body(true);
                    } else {
                        log.error("Passwords did not match");
                        return ok().body(false);
                    }
                }
            }
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @CachePut(cacheNames = "user", key = "#input.id")
    @PutMapping
    public ResponseEntity<UserDtoOutput> update(@RequestBody UserDtoInput input) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            User user = userService.update(userConverter.dtoInputToEntity(input));
            UserDtoOutput output = userConverter.entityToDtoOutput(user);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName() , this.getClass().getSimpleName());
            return ok(output);
        } catch (BadRequestException e) {
            log.error(e + this.getClass().getSimpleName());
            return badRequest().build();
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @CrossOrigin(exposedHeaders = "*")
    @PostMapping("/login")
    public ResponseEntity<UserDtoOutput> loginUser(@RequestBody UserDtoInput input) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            User user = userService.getUserByEmail(input.getEmail());
            if (user == null) return notFound().build();
            else {
                if (passwordEncoder.matches(input.getPassword(), user.getPassword())) {
                    UserDtoOutput output = userConverter.entityToDtoOutput(user);

                    String accessToken = userService.createAccessToken(user);
                    HttpHeaders responseHeaders = new HttpHeaders();
                    responseHeaders.set(ACCESS_TOKEN, accessToken);
                    log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName()  , this.getClass().getSimpleName());
                    return ok().headers(responseHeaders).body(output);
                } else return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @GetMapping("/getUserByToken")
    public ResponseEntity<UserDtoOutput> getUserByToken(@RequestHeader("Authorization") String token) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            User user = authService.getUserByAuthToken(token);
            UserDtoOutput output = userConverter.entityToDtoOutput(user);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName()  , this.getClass().getSimpleName());
            return ok(output);
        } catch (InvalidTokenException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.RESET_CONTENT);
            return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
        } catch (ExpiredTokenException ex) {
            log.error(ex + this.getClass().getSimpleName(), HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @GetMapping("/verify/{verificationCode}")
    public ResponseEntity<Boolean> verify(@PathVariable String verificationCode) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        return ok(userService.verify(verificationCode));
    }

    @GetMapping("/resend-verification/{userId}")
    public ResponseEntity<UserDtoOutput> resendVerification(@PathVariable String userId) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName() , this.getClass().getSimpleName());
        try {
            User user = userService.resendVerificationCode(userId);
            UserDtoOutput output = userConverter.entityToDtoOutput(user);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName()  , this.getClass().getSimpleName());
            return ok(output);
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @GetMapping("/reset-password/{email}")
    public ResponseEntity<Void> resetPassword(@PathVariable String email) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            userService.resetPassword(email);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName()  , this.getClass().getSimpleName());
            return ok().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(
            @RequestHeader("Authorization") String authToken,
            @RequestBody PasswordPayload passwordPayload
    ) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            User user = authService.getUserByAuthToken(authToken);
            boolean passwordChanged = userService.changePassword(user, passwordPayload.getCurrentPassword(), passwordPayload.getNewPassword());
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName() , this.getClass().getSimpleName());
            return ok(passwordChanged);
        } catch (InvalidTokenException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.RESET_CONTENT);
            return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
        } catch (ExpiredTokenException ex) {
            log.error(ex + this.getClass().getSimpleName(), HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (BadRequestException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @PostMapping("/user-feedback")
    public ResponseEntity<Boolean> getUserFeedback(
            @RequestHeader("Authorization") String authToken,
            @RequestBody FeedbackDto feedbackDto
            ) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            User user = authService.getUserByAuthToken(authToken);
            boolean feedbackCorrect = userService.getUserFeedback(user, feedbackDto);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(feedbackCorrect);
        } catch (InvalidTokenException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.RESET_CONTENT);
            return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
        } catch (ExpiredTokenException ex) {
            log.error(ex + this.getClass().getSimpleName(), HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

}
