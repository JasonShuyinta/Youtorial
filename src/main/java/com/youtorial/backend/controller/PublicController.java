package com.youtorial.backend.controller;

import com.youtorial.backend.model.Account.User;
import com.youtorial.backend.model.Account.UserConverter;
import com.youtorial.backend.model.Account.UserDtoOutput;
import com.youtorial.backend.model.Publics.Public;
import com.youtorial.backend.model.Publics.PublicConverter;
import com.youtorial.backend.model.Publics.PublicDtoOutput;
import com.youtorial.backend.model.Tutorial.TutorialConverter;
import com.youtorial.backend.model.Tutorial.TutorialDtoInput;
import com.youtorial.backend.model.utils.Rating;
import com.youtorial.backend.service.PublicService;
import com.youtorial.backend.utils.exception.ExpiredTokenException;
import com.youtorial.backend.utils.exception.InvalidTokenException;
import com.youtorial.backend.utils.exception.NotFoundsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.youtorial.backend.utils.Constants.*;
import static org.springframework.http.ResponseEntity.*;

@Controller
@Slf4j
@RequestMapping("/api/v1/public")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PublicController {

    private final PublicService publicService;
    private final UserConverter userConverter;
    private final PublicConverter publicConverter;
    private final TutorialConverter tutorialConverter;

    @GetMapping("/getTutorialAuthor/{videoId}")
    public ResponseEntity<String> getTutorialsAuthors(@PathVariable String videoId) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            String userId = publicService.getTutorialAuthor(videoId);
            if (userId != null) {
                log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return ok(userId);
            } else return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @PostMapping("/publish")
    public ResponseEntity<PublicDtoOutput> publishTutorial(@RequestBody TutorialDtoInput input) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Public pub = publicService.publishTutorial(tutorialConverter.dtoInputToEntity(input));
            PublicDtoOutput output = publicConverter.entityToDtoOutput(pub);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(output);
        } catch (NotFoundsException ue) {
            log.error(ue.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @DeleteMapping("/{publicTutorialId}")
    public ResponseEntity<Boolean> deletePublicTutorial(@PathVariable String publicTutorialId) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            boolean result = publicService.delete(publicTutorialId);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(result);
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @GetMapping("/isAuthor/{videoId}/{userId}")
    public ResponseEntity<Boolean> isAuthor(@PathVariable("videoId") String videoId,
                                            @PathVariable("userId") String userId) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Boolean isAuthor = publicService.isAuthor(videoId, userId);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(isAuthor);
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @GetMapping("/getPublicTutorial/{videoId}")
    public ResponseEntity<PublicDtoOutput> getPublicTutorial(@PathVariable("videoId") String videoId) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Public pub = publicService.getPublicByVideoId(videoId);
            if (pub != null) {
                PublicDtoOutput output = publicConverter.entityToDtoOutput(pub);
                log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return ok(output);
            } else return noContent().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @GetMapping("/getTutorialRating/{videoId}/{authorId}")
    public ResponseEntity<Rating> getTutorialRating(@PathVariable("videoId") String videoId,
                                                    @PathVariable("authorId") String authorId) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Rating rating = publicService.getRating(videoId, authorId);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(rating);
        } catch (NotFoundsException e) {
            log.error(e.getMessage() + " " + this.getClass().getSimpleName());
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @GetMapping("/getUserTutorials")
    public ResponseEntity<List<PublicDtoOutput>> getUserTutorials(@RequestHeader("Authorization") String token) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            List<Public> publicList = publicService.getPublicByAuthor(token);
            List<PublicDtoOutput> output = new ArrayList<>();
            if (publicList.isEmpty()) return noContent().build();
            else {
                for (Public p : publicList) {
                    output.add(publicConverter.entityToDtoOutput(p));
                }
                log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return ok(output);
            }
        } catch (InvalidTokenException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (ExpiredTokenException ex) {
            log.error(ex + this.getClass().getSimpleName(), HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @GetMapping("/getAuthorByTutorialId/{tutorialId}")
    public ResponseEntity<UserDtoOutput> getAuthorByTutorialId(@PathVariable String tutorialId) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            User user = publicService.getAuthor(tutorialId);
            if (user != null) {
                UserDtoOutput output = userConverter.entityToDtoOutput(user);
                log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return ok(output);
            } else return badRequest().build();
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }
}
