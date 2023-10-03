package com.youtorial.backend.service;

import com.youtorial.backend.model.Account.User;
import com.youtorial.backend.model.Publics.Public;
import com.youtorial.backend.model.Step.Step;
import com.youtorial.backend.model.Tutorial.Tutorial;
import com.youtorial.backend.model.utils.PayloadDeleteStep;
import com.youtorial.backend.repository.PublicRepository;
import com.youtorial.backend.repository.StepRepository;
import com.youtorial.backend.repository.TutorialRepository;
import com.youtorial.backend.utils.exception.ExpiredTokenException;
import com.youtorial.backend.utils.exception.InvalidTokenException;
import com.youtorial.backend.utils.exception.NotFoundsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.youtorial.backend.utils.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TutorialService {

    private final TutorialRepository tutorialRepository;
    private final StepRepository stepRepository;
    private final AuthService authService;
    private final PublicRepository publicRepository;

    public List<Tutorial> getTutorials() {
        log.info("GetTutorials in {}", this.getClass().getSimpleName());
        try {
            return tutorialRepository.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }

    public Tutorial save(Tutorial tutorial, String token) throws InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            User user = authService.getUserByAuthToken(token);
            tutorial.setAuthor(user.getId());
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return tutorialRepository.save(tutorial);
        } catch (InvalidTokenException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new InvalidTokenException(e.getMessage());
        } catch (ExpiredTokenException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new ExpiredTokenException(e.getMessage());
        } catch (Exception e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName());
        }
    }

    public void delete(String id) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            if (!tutorialRepository.existsById(id)) {
                log.error(NOT_FOUND_ERROR + this.getClass());
                throw new NotFoundsException(NO_TUTORIAL_WAS_FOUND);
            }
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            tutorialRepository.deleteById(id);
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass(), e);
        }
    }


    public Tutorial update(Tutorial tutorial) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            Optional<Tutorial> optionalTutorial = tutorialRepository.findById(tutorial.getId());
            if (optionalTutorial.isPresent()) {
                optionalTutorial.get().setSteps(tutorial.getSteps());
                optionalTutorial.get().setAuthor(tutorial.getAuthor());
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return tutorialRepository.save(optionalTutorial.get());
            } else {
                log.error(NOT_FOUND_ERROR + this.getClass().getSimpleName());
                throw new NotFoundsException(NO_TUTORIAL_WAS_FOUND);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }


    //    @Cacheable(value = "tutorial", key = "#id")
    public Tutorial getTutorialById(String id) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            Optional<Tutorial> optionalTutorial = tutorialRepository.findById(id);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return optionalTutorial.orElse(null);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }


    public Tutorial saveStep(Step step, String token) throws InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        log.info("TEST");
        try {
            User author = authService.getUserByAuthToken(token);
            step.setUploadDate(LocalDateTime.now());
            stepRepository.save(step);
            List<Step> steps;
            Optional<Tutorial> optionalTutorial = tutorialRepository.findTutorialByVideoId(step.getVideoId());
            if (optionalTutorial.isEmpty()) {
                steps = new ArrayList<>();
                steps.add(step);
                Tutorial tutorial = new Tutorial();
                tutorial.setVideoId(step.getVideoId());
                tutorial.setSteps(steps);
                tutorial.setAuthor(author.getId());
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return tutorialRepository.save(tutorial);
            } else {
                Tutorial tutorial = optionalTutorial.get();
                steps = tutorial.getSteps();
                steps.add(step);
                tutorial.setSteps(steps);
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return tutorialRepository.save(tutorial);
            }
        } catch (InvalidTokenException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new InvalidTokenException(e.getMessage());
        } catch (ExpiredTokenException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new ExpiredTokenException(e.getMessage());
        } catch (Exception e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName());
        }
    }


    public Step updateStep(String stepId, Step step) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            Optional<Step> optionalStep = stepRepository.findById(stepId);
            if (optionalStep.isPresent()) {
                Step stepToModify = optionalStep.get();
                String oldId = stepToModify.getId();
                stepToModify = step;
                stepToModify.setId(oldId);
                stepToModify.setUploadDate(LocalDateTime.now());
                Step updatedStep = stepRepository.save(stepToModify);

                Optional<Tutorial> optionalTutorial = tutorialRepository.findTutorialByVideoId(step.getVideoId());
                if (optionalTutorial.isPresent()) {
                    Tutorial tutorial = optionalTutorial.get();
                    for (int i = 0; i < tutorial.getSteps().size(); i++) {
                        if (tutorial.getSteps().get(i).getId().equals(stepId)) {
                            tutorial.getSteps().set(i, updatedStep);
                            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                            tutorialRepository.save(tutorial);
                            break;
                        }
                    }
                }
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return updatedStep;
            } else {
                log.error(NOT_FOUND_ERROR + this.getClass());
                throw new NotFoundsException("Step id " + stepId + " not found");
            }
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (Exception e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName());
        }
    }


    public int getStepsNumber(String videoId) throws InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            int stepsNumber = 0;
            Optional<Tutorial> optionalTutorial = tutorialRepository.findTutorialByVideoId(videoId);
            if (optionalTutorial.isPresent()) stepsNumber = optionalTutorial.get().getSteps().size();
            else {
                log.error(NOT_FOUND_ERROR + this.getClass().getSimpleName());
                throw new NotFoundsException(NO_TUTORIAL_WAS_FOUND);
            }
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return stepsNumber;
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName());
        }
    }


    public int getStepsNumberByTutorialId(String tutorialId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            Optional<Tutorial> optionalTutorial = tutorialRepository.findById(tutorialId);
            if (optionalTutorial.isPresent()) {
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return optionalTutorial.get().getSteps().size();
            } else {
                log.error(NOT_FOUND_ERROR + this.getClass());
                throw new NotFoundsException(NO_TUTORIAL_WAS_FOUND);
            }
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName());
        }
    }


    //@Cacheable(cacheNames = "tutorialByVideoIdAndAuthor", key="{#videoId, #user.id}")
    public Tutorial getTutorialByVideoId(String videoId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            Optional<Tutorial> optionalTutorial = tutorialRepository.findTutorialByVideoId(videoId);
            return optionalTutorial.orElse(null);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }


    public String getLastTutorialEndTime(String videoId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            String endTime = "0";
            Optional<Tutorial> optionalTutorial = tutorialRepository.findTutorialByVideoId(videoId);
            if (optionalTutorial.isPresent()) {
                Tutorial tutorial = optionalTutorial.get();
                int stepsNum = tutorial.getSteps().size();
                if (stepsNum > 0) endTime = tutorial.getSteps().get(stepsNum - 1).getEndTime();
            } else {
                log.error(NOT_FOUND_ERROR + this.getClass().getSimpleName());
                throw new NotFoundsException(NO_TUTORIAL_WAS_FOUND);
            }
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return endTime;
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass(), e);
        }
    }


    public Step getStepToModify(String stepId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            Optional<Step> optionalStep = stepRepository.findById(stepId);
            if (optionalStep.isPresent()) {
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return optionalStep.get();
            } else {
                log.error(NOT_FOUND_ERROR + this.getClass().getSimpleName());
                throw new NotFoundsException("No step was found");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass(), e);
        }
    }


    public Tutorial deleteStep(PayloadDeleteStep payload, String token) throws InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            Tutorial tutorialResponse;
            User user = authService.getUserByAuthToken(token);
            Optional<Tutorial> optionalTutorial = tutorialRepository.findById(payload.getTutorialId());
            if (optionalTutorial.isPresent() && user != null) {
                Tutorial tutorial = optionalTutorial.get();
                List<Step> steps = tutorial.getSteps();
                for (int i = 0; i < steps.size(); i++) {
                    if (steps.get(i).getId().equals(payload.getStepId())) {
                        steps.remove(i);
                        break;
                    }
                }
                tutorial.setSteps(steps);
                stepRepository.deleteById(payload.getStepId());
                tutorialResponse = tutorialRepository.save(tutorial);
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return tutorialResponse;
            } else {
                log.error(NOT_FOUND_ERROR + this.getClass().getSimpleName());
                throw new NotFoundsException(NO_TUTORIAL_WAS_FOUND);
            }
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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }


    public boolean checkDifferences(String tutorialId, String authToken) throws InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            User author = authService.getUserByAuthToken(authToken);
            boolean isSame = false;
            Optional<Tutorial> optionalTutorial = tutorialRepository.findById(tutorialId);
            if (optionalTutorial.isPresent() && author != null) {
                Public pub = publicRepository.findByTutorialId(tutorialId);
                if (pub != null)
                    isSame = optionalTutorial.get().getSteps().equals(pub.getSteps());
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return isSame;
            } else {
                log.error(HttpStatus.NOT_FOUND + this.getClass().getSimpleName());
                throw new NotFoundsException("Tutorial not found " + this.getClass().getSimpleName());
            }
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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass(), e);
        }
    }

    public void deleteTutorialByVideoId(String videoId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            tutorialRepository.deleteByVideoId(videoId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass(), e);
        }
    }

}
