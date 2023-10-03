package com.youtorial.backend.controller;

import com.youtorial.backend.model.utils.Notes;
import com.youtorial.backend.service.InteractionService;
import com.youtorial.backend.utils.exception.ExpiredTokenException;
import com.youtorial.backend.utils.exception.InvalidTokenException;
import com.youtorial.backend.utils.exception.NotFoundsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.youtorial.backend.utils.Constants.*;
import static org.springframework.http.ResponseEntity.*;

@Controller
@RequestMapping("/api/v1/interaction")
@Slf4j
@CrossOrigin("*")
@RequiredArgsConstructor
public class InteractionController {

    private final InteractionService interactionService;

    @GetMapping("/saveVideo/{videoId}/{savedBoolean}")
    public ResponseEntity<Void> saveVideo(@PathVariable("videoId") String videoId,
                                          @PathVariable("savedBoolean") boolean savedBoolean,
                                          @RequestHeader("Authorization") String authToken) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            interactionService.saveVideo(videoId, savedBoolean, authToken);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok().build();
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

    @GetMapping("/checkStep/{videoId}/{stepId}/{checkBoolean}")
    public ResponseEntity<Void> checkStep(@PathVariable("videoId") String videoId,
                                          @PathVariable("stepId") String stepId,
                                          @PathVariable("checkBoolean") boolean checkBoolean,
                                          @RequestHeader("Authorization") String authToken) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            interactionService.checkStep(videoId, stepId, checkBoolean, authToken);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok().build();
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

    @GetMapping("/getCheckedStep/{videoId}/{stepId}")
    public ResponseEntity<Boolean> getCheckedStep(@PathVariable String videoId,
                                                  @PathVariable String stepId,
                                                  @RequestHeader("Authorization") String authToken) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            boolean isChecked = interactionService.getCheckedStep(videoId, stepId, authToken);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(isChecked);
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

    @PostMapping("/saveNotes/{videoId}/{stepId}")
    public ResponseEntity<Void> saveNotes(@PathVariable("videoId") String videoId,
                                          @PathVariable("stepId") String stepId,
                                          @RequestBody Notes notes,
                                          @RequestHeader("Authorization") String authToken) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            interactionService.saveNotes(videoId, stepId, authToken, notes);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok().build();
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

    @GetMapping("/getSavedNotes/{videoId}/{stepId}")
    public ResponseEntity<String> getSavedNotes(@PathVariable("videoId") String videoId,
                                                @PathVariable("stepId") String stepId,
                                                @RequestHeader("Authorization") String authToken) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            String notes = interactionService.getNotes(videoId, stepId, authToken);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(notes);
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

    @GetMapping("/getCompletionPercentage/{videoId}")
    public ResponseEntity<Integer> getCompletionPercentage(@PathVariable String videoId,
                                                           @RequestHeader("Authorization") String authToken) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            int percentage = interactionService.getCompletionPercentage(videoId, authToken);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(percentage);
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

    @GetMapping("/subscribe/{authorId}")
    public ResponseEntity<Void> subscribeChannel(@PathVariable String authorId,
                                                 @RequestHeader("Authorization") String authToken) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            interactionService.subscribeChannel(authorId, authToken);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok().build();
        } catch (InvalidTokenException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (ExpiredTokenException ex) {
            log.error(ex + this.getClass().getSimpleName(), HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @GetMapping("/unsubscribe/{authorId}")
    public ResponseEntity<Void> unsubscribeChannel(@PathVariable String authorId,
                                                   @RequestHeader("Authorization") String authToken) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            interactionService.unsubscribeChannel(authorId, authToken);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok().build();
        } catch (NotFoundsException nf) {
            log.error(ERROR, nf, this.getClass().getSimpleName());
            throw new NotFoundsException(nf.getMessage());
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

    @GetMapping("/isSubscribed/{authorId}")
    public ResponseEntity<Boolean> isSubscribed(@PathVariable String authorId,
                                                @RequestHeader("Authorization") String authToken) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            boolean isSubbed = interactionService.isSubscribed(authorId, authToken);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(isSubbed);
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

    @GetMapping("/getFollowersNumber")
    public ResponseEntity<Integer> getFollowersNumber(@RequestHeader("Authorization") String authToken) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            int num = interactionService.getFollowersNum(authToken);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(num);
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

    @GetMapping("/getSubscriptions")
    public ResponseEntity<List<String>> getSubscriptions(@RequestHeader("Authorization") String authToken) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            List<String> subs = interactionService.getSubscriptions(authToken);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(subs);
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

    /********* START UNUSED VIDEO *********/
    @GetMapping("/rateVideo/{videoId}/{ratingValue}")
    public ResponseEntity<Void> rateVideo(@PathVariable("videoId") String videoId,
                                          @PathVariable("ratingValue") int ratingValue,
                                          @RequestHeader("Authorization") String authToken) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            interactionService.rateVideo(videoId, ratingValue, authToken);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok().build();
        } catch (InvalidTokenException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (ExpiredTokenException ex) {
            log.error(ex + this.getClass().getSimpleName(), HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR);
            return internalServerError().build();
        }
    }

    @GetMapping("/getRating/{videoId}")
    public ResponseEntity<Integer> getRating(@PathVariable String videoId,
                                             @RequestHeader("Authorization") String authToken) {
        log.info(START ,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            int rating = interactionService.getRating(videoId, authToken);
            log.info(END,Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(rating);
        } catch (InvalidTokenException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (ExpiredTokenException ex) {
            log.error(ex + this.getClass().getSimpleName(), HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR);
            return internalServerError().build();
        }
    }

    /********* END UNUSED VIDEO *********/
}
