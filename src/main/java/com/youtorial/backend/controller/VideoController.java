package com.youtorial.backend.controller;

import com.youtorial.backend.model.Video.Video;
import com.youtorial.backend.model.Video.VideoConverter;
import com.youtorial.backend.model.Video.VideoDtoInput;
import com.youtorial.backend.model.Video.VideoDtoOutput;
import com.youtorial.backend.service.VideoService;
import com.youtorial.backend.utils.exception.ExpiredTokenException;
import com.youtorial.backend.utils.exception.InvalidTokenException;
import com.youtorial.backend.utils.exception.NotFoundsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.youtorial.backend.utils.Constants.*;
import static org.springframework.http.ResponseEntity.*;

@Controller
@RequestMapping("/api/v1/video")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class VideoController {

    private final VideoService videoService;
    private final VideoConverter videoConverter;

    @GetMapping
    public ResponseEntity<List<VideoDtoOutput>> findAll() {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            List<Video> videos = videoService.getVideos();
            if (videos.isEmpty()) return noContent().build();
            else {
                List<VideoDtoOutput> videoDtoOutputs = new ArrayList<>();
                for (Video video : videos)
                    videoDtoOutputs.add(videoConverter.entityToDtoOutput(video));
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return ok(videoDtoOutputs);
            }
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @GetMapping("/videoInfo/{id}")
    public ResponseEntity<VideoDtoOutput> findVideo(@PathVariable String id) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Video video = videoService.getVideoById(id);
            if (video != null) {
                VideoDtoOutput dtoOutput = videoConverter.entityToDtoOutput(video);
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return ok(dtoOutput);
            } else return noContent().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @PostMapping
    @CrossOrigin("*")
    public ResponseEntity<VideoDtoOutput> save(@RequestBody VideoDtoInput input,
                                               @RequestHeader("Authorization") String token) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Video video = videoService.save(videoConverter.dtoInputToEntity(input), token);
            VideoDtoOutput output = videoConverter.entityToDtoOutput(video);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(output);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            videoService.delete(id);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(id);
        } catch (NotFoundsException nf) {
            log.error(nf + this.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @PutMapping
    public ResponseEntity<VideoDtoOutput> update(@RequestBody VideoDtoInput input) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Video video = videoService.update(videoConverter.dtoInputToEntity(input));
            VideoDtoOutput output = videoConverter.entityToDtoOutput(video);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok(output);
        } catch (NotFoundsException nf) {
            log.error(nf + this.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
            return notFound().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @GetMapping("/query/{queryString}")
    public ResponseEntity<List<VideoDtoOutput>> searchByQuery(@PathVariable String queryString) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            List<Video> videos = videoService.queryVideo(queryString);
            if (videos.isEmpty()) return noContent().build();
            else {
                List<VideoDtoOutput> output = new ArrayList<>();
                for (Video video : videos) {
                    output.add(videoConverter.entityToDtoOutput(video));
                }
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return ok(output);
            }
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @PostMapping("/queryByCategory")
    public ResponseEntity<List<VideoDtoOutput>> searchByCategory(@RequestBody List<String> categories) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            List<Video> videos = videoService.queryCategory(categories);
            if (videos.isEmpty()) return noContent().build();
            else {
                List<VideoDtoOutput> output = new ArrayList<>();
                for (Video video : videos) {
                    output.add(videoConverter.entityToDtoOutput(video));
                }
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return ok(output);
            }
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @GetMapping("/videoByUser/{userId}")
    public ResponseEntity<List<VideoDtoOutput>> getVideoByUser(@PathVariable String userId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            List<Video> videos = videoService.getVideosByUser(userId);
            if (videos.isEmpty()) return noContent().build();
            else {
                List<VideoDtoOutput> output = new ArrayList<>();
                for (Video video : videos) {
                    output.add(videoConverter.entityToDtoOutput(video));
                }
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return ok(output);
            }
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

    @DeleteMapping("/deleteVideo/{videoId}")
    public ResponseEntity<Void> deleteVideo(@PathVariable String videoId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            videoService.deleteById(videoId);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return ok().build();
        } catch (Exception e) {
            log.error(EXCEPTION, e, this.getClass().getSimpleName());
            return internalServerError().build();
        }
    }

}
