package com.youtorial.backend.controller;

import com.youtorial.backend.model.Step.Step;
import com.youtorial.backend.model.Step.StepConverter;
import com.youtorial.backend.model.Step.StepDtoInput;
import com.youtorial.backend.model.Step.StepDtoOutput;
import com.youtorial.backend.model.Tutorial.Tutorial;
import com.youtorial.backend.model.Tutorial.TutorialConverter;
import com.youtorial.backend.model.Tutorial.TutorialDtoInput;
import com.youtorial.backend.model.Tutorial.TutorialDtoOutput;
import com.youtorial.backend.model.utils.PayloadDeleteStep;
import com.youtorial.backend.service.TutorialService;
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
@RequestMapping("/api/v1/tutorial")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class TutorialController {

    private final TutorialService tutorialService;
    private final TutorialConverter tutorialConverter;
    private final StepConverter stepConverter;

    /* */
    @PostMapping("/saveStep")
    public ResponseEntity<TutorialDtoOutput> saveStep(@RequestBody StepDtoInput stepInput,
                                                      @RequestHeader("Authorization") String token) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Step step = stepConverter.dtoInputToEntity(stepInput);
            Tutorial tutorial = tutorialService.saveStep(step, token);
            TutorialDtoOutput output = tutorialConverter.entityToDtoOutput(tutorial);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(output);
        } catch (InvalidTokenException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (ExpiredTokenException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    /*update*/
    @PutMapping("/updateStep/{stepId}")
    public ResponseEntity<StepDtoOutput> updateStep(@PathVariable String stepId,
                                                    @RequestBody StepDtoInput stepDtoInput) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Step step = tutorialService.updateStep(stepId, stepConverter.dtoInputToEntity(stepDtoInput));
            StepDtoOutput output = stepConverter.entityToDtoOutput(step);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(output);
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    /*getStepsNumber with videoID*/
    @GetMapping("/getStepsNumber/{videoId}")
    public ResponseEntity<Integer> getStepsNumber(@PathVariable String videoId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            int num = tutorialService.getStepsNumber(videoId);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(num);
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @GetMapping("/getStepsNumberTutorialId/{tutorialId}")
    public ResponseEntity<Integer> getStepsNumberTutorialId(@PathVariable String tutorialId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            int stepsNumber = tutorialService.getStepsNumberByTutorialId(tutorialId);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(stepsNumber);
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    /*getSelectedTutorial*/
    @GetMapping("/getSelectedTutorial/{videoId}")
    public ResponseEntity<TutorialDtoOutput> getSelectedTutorial(@PathVariable String videoId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Tutorial tutorial = tutorialService.getTutorialByVideoId(videoId);
            if (tutorial != null) {
                TutorialDtoOutput output = tutorialConverter.entityToDtoOutput(tutorial);
                return ok(output);
            } else return noContent().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    /*getLastTutorial*/
    @GetMapping("/getLastTutorialEndTime/{videoId}")
    public ResponseEntity<String> getLastTutorial(@PathVariable String
                                                          videoId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            String endTime = tutorialService.getLastTutorialEndTime(videoId);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(endTime);
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @PostMapping("/deleteStep")
    public ResponseEntity<TutorialDtoOutput> deleteStep(@RequestBody PayloadDeleteStep payload,
                                                        @RequestHeader("Authorization") String token) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Tutorial tutorial = tutorialService.deleteStep(payload, token);
            TutorialDtoOutput output = tutorialConverter.entityToDtoOutput(tutorial);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(output);
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
            return notFound().build();
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

    @GetMapping("/checkDifferences/{tutorialId}")
    public ResponseEntity<Boolean> checkDifferences(@PathVariable String tutorialId,
                                                    @RequestHeader("Authorization") String authToken) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            boolean isDifferent = tutorialService.checkDifferences(tutorialId, authToken);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(isDifferent);
        } catch (NotFoundsException ue) {
            log.error(ue.toString());
            return notFound().build();
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

    @GetMapping
    public ResponseEntity<List<TutorialDtoOutput>> findAll() {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            List<Tutorial> tutorials = tutorialService.getTutorials();
            if (tutorials.isEmpty()) return noContent().build();
            else {
                List<TutorialDtoOutput> tutorialDtoOutputs = new ArrayList<>();
                for (Tutorial tutorial : tutorials)
                    tutorialDtoOutputs.add(tutorialConverter.entityToDtoOutput(tutorial));
                return ok(tutorialDtoOutputs);
            }
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TutorialDtoOutput> findTutorial(@PathVariable String id) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Tutorial tutorial = tutorialService.getTutorialById(id);
            if (tutorial != null) {
                TutorialDtoOutput dtoOutput = tutorialConverter.entityToDtoOutput(tutorial);
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return ok(dtoOutput);
            } else return noContent().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<TutorialDtoOutput> save(@RequestBody TutorialDtoInput input,
                                                  @RequestHeader("Authorization") String token) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Tutorial tutorial = tutorialConverter.dtoInputToEntity(input);
            tutorialService.save(tutorial, token);
            TutorialDtoOutput output = tutorialConverter.entityToDtoOutput(tutorial);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(output);
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            tutorialService.delete(id);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(id);
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @PutMapping
    public ResponseEntity<TutorialDtoOutput> update(@RequestBody TutorialDtoInput input) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Tutorial tutorial = tutorialConverter.dtoInputToEntity(input);
            tutorialService.update(tutorial);
            TutorialDtoOutput output = tutorialConverter.entityToDtoOutput(tutorial);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(output);
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }


}