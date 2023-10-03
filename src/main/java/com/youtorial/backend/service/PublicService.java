package com.youtorial.backend.service;

import com.youtorial.backend.model.Account.User;
import com.youtorial.backend.model.Publics.Public;
import com.youtorial.backend.model.Tutorial.Tutorial;
import com.youtorial.backend.model.Video.Video;
import com.youtorial.backend.model.utils.Rating;
import com.youtorial.backend.repository.PublicRepository;
import com.youtorial.backend.repository.UserRepository;
import com.youtorial.backend.repository.VideoRepository;
import com.youtorial.backend.utils.exception.ExpiredTokenException;
import com.youtorial.backend.utils.exception.InvalidTokenException;
import com.youtorial.backend.utils.exception.NotFoundsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.youtorial.backend.utils.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicService {

    private final PublicRepository publicRepository;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final AuthService authService;


    public String getTutorialAuthor(String videoId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Optional<Video> optionalVideo = videoRepository.findById(videoId);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return optionalVideo.map(Video::getAuthor).orElse(null);
        } catch (Exception e) {
            log.error(e + this.getClass().getSimpleName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }

    public Public publishTutorial(Tutorial tutorial) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Public publics;
            Public pub = publicRepository.findPublicByTutorialId(tutorial.getId());
            Optional<Video> optionalVideo = videoRepository.findById(tutorial.getVideoId());
            if (pub == null) {
                pub = new Public();
                pub.setAuthor(tutorial.getAuthor());
                pub.setVideoId(tutorial.getVideoId());
                pub.setSteps(tutorial.getSteps());
                pub.setTutorialId(tutorial.getId());
                publics = publicRepository.save(pub);
                if (optionalVideo.isPresent()) {
                    Video video = optionalVideo.get();
                    video.setTutorialId(tutorial.getId());
                    videoRepository.save(video);
                } else {
                    log.error(NOT_FOUND_ERROR + this.getClass().getSimpleName());
                    throw new NotFoundsException("No video found ");
                }
            } else {
                pub.setSteps(tutorial.getSteps());
                publics = publicRepository.save(pub);
            }
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return publics;
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }


    @Cacheable(cacheNames = "isAuthor", key = "{#videoId, #author}")
    public Boolean isAuthor(String videoId, String author) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            boolean isAuthor;
            Optional<Video> optionalVideo = videoRepository.findById(videoId);
            Optional<User> optionalUser = userRepository.findById(author);
            if (optionalUser.isPresent()) {
                if (optionalVideo.isPresent()) {
                    isAuthor = optionalVideo.get().getAuthor().equals(optionalUser.get().getId());
                } else {
                    log.error(NOT_FOUND_ERROR + this.getClass().getSimpleName());
                    throw new NotFoundsException("Could not find video with id " + videoId);
                }
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return isAuthor;
            } else {
                log.error(NOT_FOUND_ERROR + this.getClass().getSimpleName());
                throw new NotFoundsException("Could not find user with id " + author);
            }
        } catch (NotFoundsException e) {
            log.error(NOT_FOUND_ERROR + this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }


    public Public getPublicByVideoId(String videoId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Optional<Public> optionalPublic = publicRepository.findPublicByVideoId(videoId);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return optionalPublic.orElse(null);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass(), e);
        }
    }


    public Rating getRating(String videoId, String authorId) throws NotFoundsException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Rating rating = new Rating();
            Optional<Video> optionalVideo = videoRepository.findById(videoId);
            if (optionalVideo.isPresent()) {
                Video video = optionalVideo.get();
                if (video.getTutorialId() != null) {
                    Optional<Public> optionalPublic = publicRepository.findById(video.getTutorialId());
                    if (optionalPublic.isPresent()) setRatingValues(authorId, rating, optionalPublic.get());
                    else throw new NotFoundsException("Public tutorial not found");
                }
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return rating;
            } else {
                log.error(NOT_FOUND_ERROR + " video with id videoID " + this.getClass().getSimpleName());
                throw new NotFoundsException("No video found with id " + videoId);
            }
        } catch (NotFoundsException e) {
            log.error(NOT_FOUND_ERROR + this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass(), e);
        }
    }

    private void setRatingValues(String authorId, Rating rating, Public p) {
        if (p.getAuthor().equals(authorId) && p.getUserRating() != null && !p.getUserRating().isEmpty()) {
            int sum = makeRatingSum(p.getUserRating());
            rating.setSum(sum / p.getUserRating().size());
            rating.setRatingNum(p.getUserRating().size());
        } else {
            rating.setSum(0);
            rating.setRatingNum(0);
        }
    }


    public List<Public> getPublicByAuthor(String token) throws InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            User user = authService.getUserByAuthToken(token);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return publicRepository.findPublicByAuthor(user.getId());
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


    public User getAuthor(String tutorialId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Optional<Public> optionalPublic = publicRepository.findById(tutorialId);
            if (optionalPublic.isPresent()) {
                Optional<User> user = userRepository.findById(optionalPublic.get().getAuthor());
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                return user.orElse(null);
            } else {
                log.error(NOT_FOUND_ERROR + this.getClass().getSimpleName());
                throw new NotFoundsException("optionalPublic not found ");
            }
        } catch (NotFoundsException e) {
            log.error(NOT_FOUND_ERROR + this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }

    public boolean delete(String publicTutorialId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            if (!publicRepository.existsById(publicTutorialId)) {
                log.error(NOT_FOUND_ERROR + this.getClass().getSimpleName());
                throw new NotFoundsException("Could not find public tutorial with id " + publicTutorialId);
            }
            publicRepository.deleteById(publicTutorialId);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return true;
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }

    public void deleteByVideoId(String videoId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            publicRepository.deleteByVideoId(videoId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }

    public int makeRatingSum(List<String> userRating) {
        int sum = 0;
        for (String s : userRating) sum = sum + Integer.parseInt(s);
        return sum;
    }
}
