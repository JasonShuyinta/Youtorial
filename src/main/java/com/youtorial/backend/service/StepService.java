package com.youtorial.backend.service;

import com.youtorial.backend.model.Step.Step;
import com.youtorial.backend.repository.StepRepository;
import com.youtorial.backend.utils.exception.NotFoundsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.youtorial.backend.utils.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class StepService {

    private final StepRepository stepRepository;

    public List<Step> getSteps() {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return stepRepository.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }

    public Step save(Step step) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            step.setUploadDate(LocalDateTime.now());
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return stepRepository.save(step);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }


    public void delete(String id) throws NotFoundsException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            if (!stepRepository.existsById(id)) {
                log.error(NOT_FOUND_ERROR + this.getClass());
                throw new NotFoundsException(NO_STEP_FOUND);
            }
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            stepRepository.deleteById(id);
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            throw new NotFoundsException(NO_STEP_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass(), e);
        }
    }


    public Step update(Step step) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Optional<Step> optionalStep = stepRepository.findById(step.getId());
            if (optionalStep.isPresent()) {
                Step s = optionalStep.get();
                s.setTitle(step.getTitle());
                s.setEndTime(step.getEndTime());
                s.setStartTime(step.getStartTime());
                s.setLinks(step.getLinks());
                s.setInstruction(step.getInstruction());
                s.setCode(step.getCode());
                s.setLanguage(step.getLanguage());
                s.setImage(step.getImage());
                s.setIngredients(step.getIngredients());
                s.setNumPeople(step.getNumPeople());
                s.setFormula(step.getFormula());
                s.setAudioFile(step.getAudioFile());
                s.setAudioName(step.getAudioName());
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return stepRepository.save(s);
            } else {
                log.error(NOT_FOUND_ERROR + this.getClass().getSimpleName());
                throw new NotFoundsException(NO_STEP_FOUND);
            }
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }


    public Step getStepById(String id) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Optional<Step> optionalStep = stepRepository.findById(id);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return optionalStep.orElse(null);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }

    public void deleteByVideoId(String videoId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            stepRepository.deleteByVideoId(videoId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }
}
