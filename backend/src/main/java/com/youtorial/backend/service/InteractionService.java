package com.youtorial.backend.service;

import com.youtorial.backend.model.Account.User;
import com.youtorial.backend.model.Interaction.StepInteraction;
import com.youtorial.backend.model.Interaction.UserInteraction;
import com.youtorial.backend.model.Interaction.VideoInteraction;
import com.youtorial.backend.model.Interaction.VideoRating;
import com.youtorial.backend.model.Video.Video;
import com.youtorial.backend.model.utils.Notes;
import com.youtorial.backend.repository.UserInteractionRepository;
import com.youtorial.backend.repository.VideoRatingRepository;
import com.youtorial.backend.repository.VideoRepository;
import com.youtorial.backend.utils.exception.ExpiredTokenException;
import com.youtorial.backend.utils.exception.InvalidTokenException;
import com.youtorial.backend.utils.exception.NotFoundsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.youtorial.backend.utils.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class InteractionService {

    private final AuthService authService;
    private final UserInteractionRepository userInteractionRepository;
    private final VideoRepository videoRepository;
    private final VideoRatingRepository videoRatingRepository;

    public void saveVideo(String videoId, boolean savedBoolean, String authToken) throws
            InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            User user = authService.getUserByAuthToken(authToken);
            Optional<UserInteraction> optionalUserInteraction = userInteractionRepository.findUserInteractionByUserId(user.getId());
            if (optionalUserInteraction.isPresent()) {
                UserInteraction userInteraction = optionalUserInteraction.get();
                handleSaveVideoUserInterIsPresent(videoId, savedBoolean, userInteraction);
            } else {
                UserInteraction userInteraction = new UserInteraction();
                userInteraction.setUserId(user.getId());
                handleVideoInteractionListSaving(videoId, savedBoolean, userInteraction);
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

    private void handleSaveVideoUserInterIsPresent(String videoId, boolean savedBoolean, UserInteraction userInteraction) {
        if (userInteraction.getVideoInteractionList() != null && !userInteraction.getVideoInteractionList().isEmpty()) {
            for (int i = 0; i < userInteraction.getVideoInteractionList().size(); i++) {
                if (userInteraction.getVideoInteractionList().get(i).getVideoId().equals(videoId)) {
                    userInteraction.getVideoInteractionList().get(i).setIsSaved(savedBoolean);
                    userInteractionRepository.save(userInteraction);
                    break;
                }
            }
        } else handleVideoInteractionListSaving(videoId, savedBoolean, userInteraction);
    }

    private void handleVideoInteractionListSaving(String videoId, boolean savedBoolean, UserInteraction
            userInteraction) {
        List<VideoInteraction> videoInteractionList = new ArrayList<>();
        VideoInteraction videoInteraction = new VideoInteraction();
        videoInteraction.setVideoId(videoId);
        videoInteraction.setIsSaved(savedBoolean);
        videoInteractionList.add(videoInteraction);
        userInteraction.setVideoInteractionList(videoInteractionList);
        userInteractionRepository.save(userInteraction);
    }

    public void checkStep(String videoId, String stepId, boolean checkBoolean, String authToken) throws
            InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            User user = authService.getUserByAuthToken(authToken);
            Optional<UserInteraction> optionalUserInteraction = userInteractionRepository.findUserInteractionByUserId(user.getId());
            if (optionalUserInteraction.isPresent()) {
                UserInteraction userInteraction = optionalUserInteraction.get();
                if (userInteraction.getVideoInteractionList() != null && !userInteraction.getVideoInteractionList().isEmpty()) {
                    handleCheckStepVideoIntListIsPresent(videoId, stepId, checkBoolean, userInteraction);
                } else {
                    List<VideoInteraction> videoInteractionList = new ArrayList<>();
                    List<StepInteraction> stepInteractionList = new ArrayList<>();
                    StepInteraction stepInteraction = new StepInteraction();
                    stepInteraction.setStepId(stepId);
                    stepInteraction.setIsChecked(checkBoolean);
                    stepInteractionList.add(stepInteraction);
                    VideoInteraction videoInteraction = new VideoInteraction();
                    videoInteraction.setVideoId(videoId);
                    videoInteraction.setStepInteractionList(stepInteractionList);
                    videoInteractionList.add(videoInteraction);
                    userInteraction.setVideoInteractionList(videoInteractionList);
                    log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                    userInteractionRepository.save(userInteraction);
                }
            } else {
                UserInteraction userInteraction = new UserInteraction();
                userInteraction.setUserId(user.getId());
                List<VideoInteraction> videoInteractionList = new ArrayList<>();
                VideoInteraction videoInteraction = new VideoInteraction();
                videoInteraction.setVideoId(videoId);
                List<StepInteraction> stepInteractionList = new ArrayList<>();
                StepInteraction stepInteraction = new StepInteraction();
                stepInteraction.setStepId(stepId);
                stepInteraction.setIsChecked(checkBoolean);
                stepInteractionList.add(stepInteraction);
                videoInteraction.setStepInteractionList(stepInteractionList);
                videoInteractionList.add(videoInteraction);
                userInteraction.setVideoInteractionList(videoInteractionList);
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                userInteractionRepository.save(userInteraction);
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

    private void handleCheckStepVideoIntListIsPresent(String videoId, String stepId, boolean checkBoolean, UserInteraction userInteraction) {
        List<VideoInteraction> videoInteractionList = userInteraction.getVideoInteractionList();
        boolean videoInteractionIsPresent = false;
        VideoInteraction videoInteraction = null;
        for (VideoInteraction interaction : videoInteractionList) {
            if (interaction.getVideoId().equals(videoId)) {
                videoInteractionIsPresent = true;
                videoInteraction = interaction;
                break;
            }
        }
        if (videoInteractionIsPresent) {
            handleCheckStepVideoIsPresent(stepId, checkBoolean, userInteraction, videoInteraction);
        } else {
            videoInteraction = new VideoInteraction();
            videoInteraction.setVideoId(videoId);
            List<StepInteraction> stepInteractionList = new ArrayList<>();
            StepInteraction stepInteraction = new StepInteraction();
            stepInteraction.setIsChecked(checkBoolean);
            stepInteraction.setStepId(stepId);
            stepInteractionList.add(stepInteraction);
            videoInteraction.setStepInteractionList(stepInteractionList);
            videoInteractionList.add(videoInteraction);
            userInteractionRepository.save(userInteraction);
        }
    }

    private void handleCheckStepVideoIsPresent(String stepId, boolean checkBoolean, UserInteraction userInteraction, VideoInteraction videoInteraction) {
        if (videoInteraction.getStepInteractionList() != null && !videoInteraction.getStepInteractionList().isEmpty()) {
            List<StepInteraction> stepInteractionList = videoInteraction.getStepInteractionList();
            boolean stepInteractionIsPresent = false;
            StepInteraction stepInteraction = null;
            for (StepInteraction interaction : stepInteractionList) {
                if (interaction.getStepId().equals(stepId)) {
                    stepInteractionIsPresent = true;
                    stepInteraction = interaction;
                    break;
                }
            }
            if (stepInteractionIsPresent) {
                stepInteraction.setIsChecked(checkBoolean);
                userInteractionRepository.save(userInteraction);
            } else {
                StepInteraction stepInt = new StepInteraction();
                stepInt.setStepId(stepId);
                stepInt.setIsChecked(checkBoolean);
                stepInteractionList.add(stepInt);
                userInteractionRepository.save(userInteraction);
            }
        } else {
            List<StepInteraction> stepInteractionList = new ArrayList<>();
            StepInteraction stepInteraction = new StepInteraction();
            stepInteraction.setStepId(stepId);
            stepInteraction.setIsChecked(checkBoolean);
            stepInteractionList.add(stepInteraction);
            videoInteraction.setStepInteractionList(stepInteractionList);
            userInteractionRepository.save(userInteraction);
        }
    }

    public boolean getCheckedStep(String videoId, String stepId, String authToken) throws
            InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            boolean isChecked = false;
            User user = authService.getUserByAuthToken(authToken);
            Optional<UserInteraction> optionalUserInteraction = userInteractionRepository.findUserInteractionByUserId(user.getId());
            if (optionalUserInteraction.isPresent()) {
                UserInteraction userInteraction = optionalUserInteraction.get();
                if (userInteraction.getVideoInteractionList() != null && !userInteraction.getVideoInteractionList().isEmpty()) {
                    List<VideoInteraction> videoInteractionList = userInteraction.getVideoInteractionList();
                    boolean videoInteractionIsPresent = false;
                    VideoInteraction videoInteraction = null;
                    for (VideoInteraction interaction : videoInteractionList) {
                        if (interaction.getVideoId().equals(videoId)) {
                            videoInteractionIsPresent = true;
                            videoInteraction = interaction;
                            break;
                        }
                    }
                    isChecked = handleVideoIntIsPresent(stepId, videoInteractionIsPresent, videoInteraction);
                }
            }
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return isChecked;
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

    private boolean handleVideoIntIsPresent(String stepId, boolean videoInteractionIsPresent, VideoInteraction videoInteraction) {
        boolean isChecked = false;
        if (videoInteractionIsPresent && videoInteraction.getStepInteractionList() != null && !videoInteraction.getStepInteractionList().isEmpty()) {
            List<StepInteraction> stepInteractionList = videoInteraction.getStepInteractionList();
            for (StepInteraction stepInteraction : stepInteractionList) {
                if (stepInteraction.getStepId().equals(stepId)) {
                    isChecked = stepInteraction.getIsChecked();
                    break;
                }
            }
        }
        return isChecked;
    }

    public void saveNotes(String videoId, String stepId, String authToken, Notes notes) throws
            InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            notes.setTimestamp(timestamp.toString());
            User user = authService.getUserByAuthToken(authToken);
            Optional<UserInteraction> optionalUserInteraction = userInteractionRepository.findUserInteractionByUserId(user.getId());
            if (optionalUserInteraction.isPresent()) {
                UserInteraction userInteraction = optionalUserInteraction.get();
                handleSaveNotesUserInterIsPresent(videoId, stepId, notes, userInteraction);
            } else {
                UserInteraction userInteraction = new UserInteraction();
                userInteraction.setUserId(user.getId());
                List<VideoInteraction> videoInteractionList = new ArrayList<>();
                VideoInteraction videoInteraction = new VideoInteraction();
                videoInteraction.setVideoId(videoId);
                List<StepInteraction> stepInteractionList = new ArrayList<>();
                StepInteraction stepInteraction = new StepInteraction();
                stepInteraction.setStepId(stepId);
                stepInteraction.setNotes(notes);
                stepInteractionList.add(stepInteraction);
                videoInteraction.setStepInteractionList(stepInteractionList);
                videoInteractionList.add(videoInteraction);
                userInteraction.setVideoInteractionList(videoInteractionList);
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                userInteractionRepository.save(userInteraction);
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

    private void handleSaveNotesUserInterIsPresent(String videoId, String stepId, Notes notes, UserInteraction userInteraction) {
        if (userInteraction.getVideoInteractionList() != null &&
                !userInteraction.getVideoInteractionList().isEmpty()) {
            List<VideoInteraction> videoInteractionList = userInteraction.getVideoInteractionList();
            boolean videoInteractionIsPresent = false;
            VideoInteraction videoInt = null;
            for (VideoInteraction videoInteraction : videoInteractionList) {
                if (videoInteraction.getVideoId().equals(videoId)) {
                    videoInteractionIsPresent = true;
                    videoInt = videoInteraction;
                    break;
                }
            }
            handleVideoInteractionIsPresent(stepId, notes, userInteraction, videoInteractionIsPresent, videoInt);
        } else {
            List<VideoInteraction> videoInteractionList = new ArrayList<>();
            VideoInteraction videoInteraction = new VideoInteraction();
            videoInteraction.setVideoId(videoId);
            List<StepInteraction> stepInteractionList = new ArrayList<>();
            StepInteraction stepInteraction = new StepInteraction();
            stepInteraction.setStepId(stepId);
            stepInteraction.setNotes(notes);
            stepInteractionList.add(stepInteraction);
            videoInteraction.setStepInteractionList(stepInteractionList);
            videoInteractionList.add(videoInteraction);
            userInteraction.setVideoInteractionList(videoInteractionList);
            userInteractionRepository.save(userInteraction);
        }
    }

    private void handleVideoInteractionIsPresent(String stepId, Notes notes, UserInteraction userInteraction, boolean videoInteractionIsPresent, VideoInteraction videoInt) {
        if (!videoInteractionIsPresent) return;
        if (videoInt.getStepInteractionList() != null &&
                !videoInt.getStepInteractionList().isEmpty()) {
            List<StepInteraction> stepInteractionList = videoInt.getStepInteractionList();
            boolean stepInteractionIsPresent = false;
            StepInteraction stepInteraction = null;
            for (StepInteraction interaction : stepInteractionList) {
                if (interaction.getStepId().equals(stepId)) {
                    stepInteractionIsPresent = true;
                    stepInteraction = interaction;
                    break;
                }
            }
            if (stepInteractionIsPresent) {
                log.info("Step interaction is present");
                stepInteraction.setNotes(notes);
                userInteractionRepository.save(userInteraction);
            } else {
                log.info("Step interaction is not present");
                stepInteraction = new StepInteraction();
                stepInteraction.setNotes(notes);
                stepInteraction.setStepId(stepId);
                stepInteractionList.add(stepInteraction);
                userInteractionRepository.save(userInteraction);
            }
        } else {
            List<StepInteraction> stepInteractionList = new ArrayList<>();
            StepInteraction stepInteraction = new StepInteraction();
            stepInteraction.setStepId(stepId);
            stepInteraction.setNotes(notes);
            stepInteractionList.add(stepInteraction);
            videoInt.setStepInteractionList(stepInteractionList);
            userInteractionRepository.save(userInteraction);
        }
    }

    public String getNotes(String videoId, String stepId, String authToken) throws
            InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            String notes = null;
            User user = authService.getUserByAuthToken(authToken);
            Optional<UserInteraction> optionalUserInteraction = userInteractionRepository.findUserInteractionByUserId(user.getId());
            if (optionalUserInteraction.isPresent()) {
                UserInteraction userInteraction = optionalUserInteraction.get();
                if (userInteraction.getVideoInteractionList() != null &&
                        !userInteraction.getVideoInteractionList().isEmpty()) {
                    List<VideoInteraction> videoInteractionList = userInteraction.getVideoInteractionList();
                    boolean videoInteractionIsPresent = false;
                    VideoInteraction videoInteraction = null;
                    for (VideoInteraction videoInt : videoInteractionList) {
                        if (videoInt.getVideoId().equals(videoId)) {
                            videoInteractionIsPresent = true;
                            videoInteraction = videoInt;
                            break;
                        }
                    }
                    if (videoInteractionIsPresent &&
                            videoInteraction.getStepInteractionList() != null &&
                            !videoInteraction.getStepInteractionList().isEmpty()) {
                        List<StepInteraction> stepInteractionList = videoInteraction.getStepInteractionList();
                        for (StepInteraction stepInteraction : stepInteractionList) {
                            if (stepInteraction.getStepId().equals(stepId) &&
                                    stepInteraction.getNotes() != null &&
                                    stepInteraction.getNotes().getNote() != null) {
                                notes = stepInteraction.getNotes().getNote();
                                break;
                            }
                        }
                    }
                }
            }
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return notes;
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

    public int getCompletionPercentage(String videoId, String authToken) throws
            InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            int counter = 0;
            User user = authService.getUserByAuthToken(authToken);
            Optional<UserInteraction> optionalUserInteraction = userInteractionRepository.findUserInteractionByUserId(user.getId());
            if (optionalUserInteraction.isPresent()) {
                UserInteraction userInteraction = optionalUserInteraction.get();
                if (userInteraction.getVideoInteractionList() != null && !userInteraction.getVideoInteractionList().isEmpty()) {
                    List<VideoInteraction> videoInteractionList = userInteraction.getVideoInteractionList();
                    counter = getCounter(videoId, counter, videoInteractionList);
                }
            }
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return counter;
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

    private int getCounter(String videoId, int counter, List<VideoInteraction> videoInteractionList) {
        for (VideoInteraction videoInteraction : videoInteractionList) {
            if (videoInteraction.getVideoId().equals(videoId)) {
                if (videoInteraction.getStepInteractionList() != null &&
                        !videoInteraction.getStepInteractionList().isEmpty()) {
                    List<StepInteraction> stepInteractionList = videoInteraction.getStepInteractionList();
                    for (StepInteraction stepInteraction : stepInteractionList) {
                        if (stepInteraction.getIsChecked() != null && stepInteraction.getIsChecked())
                            counter++;
                    }
                }
                break;
            }
        }
        return counter;
    }

    public void subscribeChannel(String authorId, String authToken) throws InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            User user = authService.getUserByAuthToken(authToken);
            Optional<UserInteraction> optionalUserInteraction = userInteractionRepository.findUserInteractionByUserId(authorId);
            if (optionalUserInteraction.isPresent()) {
                UserInteraction userInteraction = optionalUserInteraction.get();
                List<String> followersId = (userInteraction.getFollowersId() != null && !userInteraction.getFollowersId().isEmpty()) ? userInteraction.getFollowersId() : new ArrayList<>();
                followersId.add(user.getId());
                userInteraction.setFollowersId(followersId);
                userInteractionRepository.save(userInteraction);
                addSubscriptionToLoggedUser(authorId, user);
            } else {
                UserInteraction userInteraction = new UserInteraction();
                userInteraction.setUserId(user.getId());
                List<String> followersId = new ArrayList<>();
                followersId.add(authorId);
                userInteraction.setFollowersId(followersId);
                log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                userInteractionRepository.save(userInteraction);
            }
        } catch (NotFoundsException e) {
            log.error(ERROR, e, this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
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

    private void addSubscriptionToLoggedUser(String authorId, User user) {
        Optional<UserInteraction> optionalLoggedUserInteraction = userInteractionRepository.findUserInteractionByUserId(user.getId());
        if (optionalLoggedUserInteraction.isPresent()) {
            UserInteraction loggedUserInteraction = optionalLoggedUserInteraction.get();
            List<String> subscriptions = (loggedUserInteraction.getSubscriptions() != null && !loggedUserInteraction.getSubscriptions().isEmpty()) ? loggedUserInteraction.getSubscriptions() : new ArrayList<>();
            subscriptions.add(authorId);
            loggedUserInteraction.setSubscriptions(subscriptions);
            userInteractionRepository.save(loggedUserInteraction);
        } else {
            UserInteraction userInteraction = new UserInteraction();
            userInteraction.setUserId(user.getId());
            List<String> subscriptions = new ArrayList<>();
            subscriptions.add(authorId);
            userInteraction.setSubscriptions(subscriptions);
            userInteractionRepository.save(userInteraction);
        }
    }

    public void unsubscribeChannel(String authorId, String authToken) throws InvalidTokenException, ExpiredTokenException, NotFoundsException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            User user = authService.getUserByAuthToken(authToken);
            Optional<UserInteraction> optionalUserInteraction = userInteractionRepository.findUserInteractionByUserId(authorId);
            if (optionalUserInteraction.isPresent()) {
                UserInteraction userInteraction = optionalUserInteraction.get();
                if (userInteraction.getFollowersId() == null)
                    throw new NotFoundsException("Could not find userInteraction.getFollowersId() ");
                else if (userInteraction.getFollowersId() != null && userInteraction.getFollowersId().isEmpty())
                    throw new NotFoundsException("FollowersId is an empty array");
                else if (userInteraction.getFollowersId() != null && !userInteraction.getFollowersId().isEmpty()) {
                    List<String> followersId = userInteraction.getFollowersId();
                    followersId.remove(user.getId());
                    userInteraction.setFollowersId(followersId);
                    log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
                    userInteractionRepository.save(userInteraction);
                }
            } else throw new NotFoundsException("Could not find userInteraction");
        } catch (NotFoundsException nf) {
            log.error(ERROR, nf, this.getClass().getSimpleName());
            throw new NotFoundsException(nf.getMessage());
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

    public boolean isSubscribed(String authorId, String authToken) throws InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            boolean isSubbed = false;
            User user = authService.getUserByAuthToken(authToken);
            Optional<UserInteraction> optionalUserInteraction = userInteractionRepository.findUserInteractionByUserId(authorId);
            if (optionalUserInteraction.isPresent()) {
                UserInteraction userInteraction = optionalUserInteraction.get();
                if (userInteraction.getFollowersId() == null || userInteraction.getUserId() != null && userInteraction.getUserId().isEmpty())
                    return false;
                else {
                    for (int i = 0; i < userInteraction.getFollowersId().size(); i++) {
                        if (userInteraction.getFollowersId().get(i).equals(user.getId())) {
                            isSubbed = true;
                            break;
                        }
                    }
                }
            }
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return isSubbed;
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


    public int getFollowersNum(String authToken) throws InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            int followersNumber = 0;
            User user = authService.getUserByAuthToken(authToken);
            if (user != null) {
                Optional<UserInteraction> optionalUserInteraction = userInteractionRepository.findUserInteractionByUserId(user.getId());
                if (optionalUserInteraction.isPresent()) {
                    UserInteraction userInteraction = optionalUserInteraction.get();
                    if (userInteraction.getFollowersId() != null)
                        followersNumber = userInteraction.getFollowersId().size();
                }
            }
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return followersNumber;
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

    public List<String> getSubscriptions(String authToken) throws InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            List<String> subs;
            User user = authService.getUserByAuthToken(authToken);
            Optional<UserInteraction> optionalUserInteraction = userInteractionRepository.findUserInteractionByUserId(user.getId());
            if (optionalUserInteraction.isPresent()) {
                UserInteraction userInteraction = optionalUserInteraction.get();
                subs = (userInteraction.getSubscriptions() != null && !userInteraction.getSubscriptions().isEmpty()) ? userInteraction.getSubscriptions() : new ArrayList<>();
            } else subs = new ArrayList<>();
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return subs;
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


    /**** START UNUSED METHODS ******/
    public void rateVideo(String videoId, int ratingValue, String authToken) throws NotFoundsException, InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            User rater = authService.getUserByAuthToken(authToken);
            Optional<UserInteraction> optionalUserInteraction = userInteractionRepository.findUserInteractionByUserId(rater.getId());
            Optional<Video> optionalVideo = videoRepository.findById(videoId);
            if (optionalVideo.isPresent()) {
                Video video = optionalVideo.get();
                if (optionalUserInteraction.isPresent()) {
                    UserInteraction userInteraction = optionalUserInteraction.get();
                    handleVideoInteractionListUpdate(ratingValue, video, userInteraction);
                } else {
                    UserInteraction userInteraction = new UserInteraction();
                    userInteraction.setUserId(rater.getId());
                    handleVideoInteractionListCreation(ratingValue, video, userInteraction);
                }
            }
            saveVideoInteraction(videoId, ratingValue);
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

    private void handleVideoInteractionListUpdate(int ratingValue, Video video, UserInteraction userInteraction) {
        if (userInteraction.getVideoInteractionList() != null && !userInteraction.getVideoInteractionList().isEmpty()) {
            for (int i = 0; i < userInteraction.getVideoInteractionList().size(); i++) {
                if (checkVideoInteraction(ratingValue, video, userInteraction, i)) break;
            }
        } else {
            handleVideoInteractionListCreation(ratingValue, video, userInteraction);
        }
    }

    private boolean checkVideoInteraction(int ratingValue, Video video, UserInteraction userInteraction, int i) {
        if (userInteraction.getVideoInteractionList().get(i).getVideoId().equals(video.getId())) {
            userInteraction.getVideoInteractionList().get(i).setRating(ratingValue);
            userInteractionRepository.save(userInteraction);
            return true;
        }
        return false;
    }


    private void handleVideoInteractionListCreation(int ratingValue, Video video, UserInteraction userInteraction) {
        List<VideoInteraction> videoInteractionList = new ArrayList<>();
        VideoInteraction videoInteraction = new VideoInteraction();
        videoInteraction.setVideoId(video.getId());
        videoInteraction.setRating(ratingValue);
        videoInteractionList.add(videoInteraction);
        userInteraction.setVideoInteractionList(videoInteractionList);
        userInteractionRepository.save(userInteraction);
    }

    private void saveVideoInteraction(String videoId, int ratingValue) {
        Optional<VideoRating> optionalVideoRating = videoRatingRepository.findVideoRatingByVideoId(videoId);
        if (optionalVideoRating.isPresent()) {
            VideoRating videoRating = optionalVideoRating.get();
            int ratingNum = videoRating.getRatingNum();
            int totalRating = videoRating.getTotalRating();
            ratingNum = ratingNum + 1;
            totalRating = totalRating + ratingValue;
            videoRating.setRatingNum(ratingNum);
            videoRating.setTotalRating(totalRating);
            videoRating.setAverageRating(totalRating / ratingNum);
            videoRatingRepository.save(videoRating);
        } else {
            VideoRating videoRating = new VideoRating();
            videoRating.setVideoId(videoId);
            videoRating.setTotalRating(ratingValue);
            videoRating.setAverageRating(ratingValue);
            videoRating.setRatingNum(1);
            videoRatingRepository.save(videoRating);
        }
    }

    public int getRating(String videoId, String authToken) throws InvalidTokenException, ExpiredTokenException {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            int rating = 0;
            User user = authService.getUserByAuthToken(authToken);
            Optional<UserInteraction> optionalUserInteraction = userInteractionRepository.findUserInteractionByUserId(user.getId());
            if (optionalUserInteraction.isPresent()) {
                UserInteraction userInteraction = optionalUserInteraction.get();
                if (userInteraction.getVideoInteractionList() != null && !userInteraction.getVideoInteractionList().isEmpty()) {
                    rating = handleRating(videoId, rating, userInteraction);
                }
            }
            return rating;
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

    private int handleRating(String videoId, int rating, UserInteraction userInteraction) {
        boolean videoInteractionIsPresent = false;
        VideoInteraction videoInteraction = null;
        for (int i = 0; i < userInteraction.getVideoInteractionList().size(); i++) {
            if (userInteraction.getVideoInteractionList().get(i).getVideoId().equals(videoId)) {
                videoInteractionIsPresent = true;
                videoInteraction = userInteraction.getVideoInteractionList().get(i);
                break;
            }
        }
        if (videoInteractionIsPresent && videoInteraction.getRating() != null)
            rating = videoInteraction.getRating();
        return rating;
    }

    /**** END UNUSED METHODS ******/
}
