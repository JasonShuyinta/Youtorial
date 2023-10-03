package com.youtorial.backend.service;

import com.youtorial.backend.model.Account.User;
import com.youtorial.backend.model.Video.Video;
import com.youtorial.backend.repository.*;
import com.youtorial.backend.utils.exception.ExpiredTokenException;
import com.youtorial.backend.utils.exception.InvalidTokenException;
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
public class VideoService {

    private final VideoRepository videoRepository;
    private final AuthService authService;
    private final StepService stepService;
    private final PublicService publicService;
    private final TutorialService tutorialService;
    private final CommentService commentService;


    public List<Video> getVideos() {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return videoRepository.findAllByOrderByUploadDateDesc();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }


    public Video save(Video video, String token) throws InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            video.setUploadDate(LocalDateTime.now());
            User user = authService.getUserByAuthToken(token);
            if (user != null) video.setAuthor(user.getId());
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return videoRepository.save(video);
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
            if (!videoRepository.existsById(id)) {
                throw new NotFoundsException("Could not find video with id " + id);
            }
            videoRepository.deleteById(id);
        } catch (NotFoundsException nf) {
            log.error(nf + this.getClass().getSimpleName());
            throw new NotFoundsException(nf.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }


    public Video update(Video video) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            Optional<Video> optionalVideo = videoRepository.findById(video.getId());
            if (optionalVideo.isPresent()) {
                Video v = optionalVideo.get();
                v.setTitle(video.getTitle());
                v.setDescription(video.getDescription());
                v.setThumbnail(video.getThumbnail());
                v.setUrl(video.getUrl());
                v.setDuration(video.getDuration());
                v.setCategory(video.getCategory());
                v.setTutorialId(video.getTutorialId());
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return videoRepository.save(v);
            } else {
                throw new NotFoundsException("Could not find video with id " + video.getId());
            }
        } catch (NotFoundsException nf) {
            log.error(NOT_FOUND_ERROR + this.getClass().getSimpleName());
            throw new NotFoundsException(nf.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }


    public Video getVideoById(String id) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            Optional<Video> optionalVideo = videoRepository.findById(id);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return optionalVideo.orElse(null);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }


    public List<Video> queryVideo(String queryString) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return videoRepository.findByTitleContainingIgnoreCase(queryString);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }


    public List<Video> queryCategory(List<String> categories) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return videoRepository.findByCategoryContaining(categories);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName());
        }
    }

    public List<Video> getVideosByUser(String userId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return videoRepository.findByAuthorOrderByUploadDateDesc(userId);
        } catch (Exception e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName());
        }
    }

    public void deleteById(String videoId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            commentService.deleteCommentByVideoId(videoId);
            stepService.deleteByVideoId(videoId);
            tutorialService.deleteTutorialByVideoId(videoId);
            publicService.deleteByVideoId(videoId);
            videoRepository.deleteById(videoId);
        } catch (Exception e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, SERVER_ERROR_IN + this.getClass().getSimpleName());
        }
    }
}
