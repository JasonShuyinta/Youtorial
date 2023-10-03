package com.youtorial.backend.controller;

import com.youtorial.backend.model.Step.Step;
import com.youtorial.backend.model.Step.StepConverter;
import com.youtorial.backend.model.Step.StepDtoInput;
import com.youtorial.backend.model.Step.StepDtoOutput;
import com.youtorial.backend.service.StepService;
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
@RequestMapping("/api/v1/step")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class StepController {

    private final StepService stepService;
    private final StepConverter stepConverter;

    @GetMapping
    public ResponseEntity<List<StepDtoOutput>> findAll() {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            List<Step> steps = stepService.getSteps();
            if (steps.isEmpty()) return noContent().build();
            else {
                List<StepDtoOutput> stepDtoOutputs = new ArrayList<>();
                for (Step step : steps)
                    stepDtoOutputs.add(stepConverter.entityToDtoOutput(step));
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return ok(stepDtoOutputs);
            }
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<StepDtoOutput> findStep(@PathVariable String id) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Step step = stepService.getStepById(id);
            if (step != null) {
                StepDtoOutput dtoOutput = stepConverter.entityToDtoOutput(step);
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return ok(dtoOutput);
            } else return noContent().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<StepDtoOutput> save(@RequestBody StepDtoInput input) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Step step = stepConverter.dtoInputToEntity(input);
            Step s = stepService.save(step);
            StepDtoOutput output = stepConverter.entityToDtoOutput(s);
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
            stepService.delete(id);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(id);
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR);
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @PutMapping
    public ResponseEntity<StepDtoOutput> update(@RequestBody StepDtoInput input) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Step step = stepConverter.dtoInputToEntity(input);
            Step s = stepService.update(step);
            StepDtoOutput output = stepConverter.entityToDtoOutput(s);
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
}
