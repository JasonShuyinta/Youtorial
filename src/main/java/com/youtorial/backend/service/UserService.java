package com.youtorial.backend.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.youtorial.backend.AppConfig;
import com.youtorial.backend.model.Account.User;
import com.youtorial.backend.model.utils.Feedback;
import com.youtorial.backend.model.utils.FeedbackDto;
import com.youtorial.backend.repository.FeedbackRepository;
import com.youtorial.backend.repository.UserRepository;
import com.youtorial.backend.utils.exception.BadRequestException;
import com.youtorial.backend.utils.exception.NotFoundsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.youtorial.backend.utils.Constants.*;

@Service
@Slf4j
@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final ResourceLoader resourceLoader;
    private final AppConfig appConfig;

    public List<User> getUsers() {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
        try {
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());
            return userRepository.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }

    public User save(User user) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            user.setSubscriptionDate(LocalDateTime.now());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEnabled(false);
            user.setUserCode(UUID.randomUUID().toString());
            user.setConfirmationCode(UUID.randomUUID().toString());
            sendVerificationEmail(user);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

            return userRepository.save(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }

    public void delete(String id) {
        log.info("Delete user {} in {}", id, this.getClass().getSimpleName());
        try {
            if (!userRepository.existsById(id)) {
                log.error(NOT_FOUND_ERROR + this.getClass());
                throw new NotFoundsException("No user was found");
            }
            userRepository.deleteById(id);
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }

    public User update(User user) {
        log.info("Update user {} in {}", user.getId(), this.getClass().getSimpleName());
        try {
            Optional<User> optionalUser = userRepository.findById(user.getId());
            if (optionalUser.isPresent()) {
                User u = optionalUser.get();
                if (usernameIsNotTaken(u, user)) {
                    u.setEmail(user.getEmail());
                    u.setFirstName(user.getFirstName());
                    u.setLastName(user.getLastName());
                    u.setUsername(user.getUsername());
                    u.setImage(user.getImage());
                    return userRepository.save(u);
                } else {
                    log.info("Username already in use");
                    throw new BadRequestException("Username already in user");
                }
            } else {
                log.error(NOT_FOUND_ERROR + this.getClass().getSimpleName());
                throw new NotFoundsException("No user was found");
            }
        } catch (BadRequestException e) {
            log.error(BAD_REQUEST_ERROR + this.getClass().getSimpleName());
            throw new BadRequestException(e.getMessage());
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }

    public String createAccessToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(appConfig.getSecretKey().getBytes());
        return JWT.create()
                .withSubject(user.getId())
                //24 h -> 24 * 60 * 60 * 1000
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .sign(algorithm);
    }

    public User getUserById(String id) {
        log.info("GetUserById with id {} in {}", id, this.getClass().getSimpleName());
        try {
            return userRepository.findById(id).orElseThrow(() -> new NotFoundsException("No user was found with id " + id));
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }

    public User getUserByUsername(String username) {
        log.info("GetUserByUsername with username {} in {}", username, this.getClass().getSimpleName());
        try {
            Optional<User> optionalUser = userRepository.findByUsername(username);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

            return optionalUser.orElse(null);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }

    public User getUserByEmail(String email) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

            return optionalUser.orElse(null);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }

    public boolean usernameIsNotTaken(User user1, User user2) {
        Optional<User> userByUsername = userRepository.findByUsername(user2.getUsername());
        if (userByUsername.isEmpty()) {
            return true;
        } else {
            return user1.getUsername().equals(userByUsername.get().getUsername());
        }
    }

    private void sendVerificationEmail(User user)
            throws MessagingException, IOException {
        log.info("sendVerificationEmail method in {}", this.getClass().getSimpleName());
        String toAddress = user.getEmail();
        String fromAddress = "youtorial@gmail.com";
        String senderName = "YouTorial";
        String subject = "Please verify your registration";
        String content = getVerificationEmailHtml();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        String verifyURL = appConfig.getVerificationUrl() + user.getConfirmationCode();
        content = content.replace("[[LOGIN]]", appConfig.getLoginUrl());
        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    private String getVerificationEmailHtml() throws IOException {
        final Resource resource = resourceLoader.getResource("classpath:templates/email.html");
        try (
                Reader reader = new InputStreamReader(resource.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(reader)
        ) {
            StringBuilder contentBuilder = new StringBuilder();
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                contentBuilder.append(str);
            }
            return contentBuilder.toString();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    private String getResetPasswordEmailHtml() throws IOException {
        final Resource resource = resourceLoader.getResource("classpath:templates/reset-password.html");
        try (
                Reader reader = new InputStreamReader(resource.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(reader)
        ) {
            StringBuilder contentBuilder = new StringBuilder();
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                contentBuilder.append(str);
            }
            return contentBuilder.toString();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public boolean verify(String verificationCode) {
        log.info("verification service in {}", this.getClass().getSimpleName());
        try {
            Optional<User> optionalUser = userRepository.findByConfirmationCode(verificationCode);
            if (optionalUser.isEmpty()) {
                return false;
            } else {
                User user = optionalUser.get();
                if (user.isEnabled()) return false;
                else {
                    user.setConfirmationCode(null);
                    user.setEnabled(true);
                    userRepository.save(user);
                    log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

                    return true;
                }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }

    public User resendVerificationCode(String userId) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundsException("No user was found with id " + userId));
            user.setConfirmationCode(UUID.randomUUID().toString());
            sendVerificationEmail(user);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

            return userRepository.save(user);
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }

    public void resetPassword(String email) {
        try {
            if (email.isEmpty()) throw new NotFoundsException("No email was present");
            else {
                User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundsException("No user was found with email " + email));
                String generatedPassword = UUID.randomUUID().toString().substring(0, 10);
                user.setPassword(passwordEncoder.encode(generatedPassword));
                userRepository.save(user);
                sendResetPasswordEmail(user, generatedPassword);
            }
        } catch (NotFoundsException e) {
            log.error(e + this.getClass().getSimpleName());
            throw new NotFoundsException(e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }

    private void sendResetPasswordEmail(User user, String password)
            throws MessagingException, IOException {
        log.info("sendVerificationEmail method in {}", this.getClass().getSimpleName());
        String toAddress = user.getEmail();
        String fromAddress = "youtorial@gmail.com";
        String senderName = "YouTorial";
        String subject = "Reset Your Password";
        String content = getResetPasswordEmailHtml();
        content = content.replace("[[PASSWORD]]", password);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    public boolean changePassword(User user, String currentPassword, String newPassword) {
        try {
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) return false;
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }

    public boolean getUserFeedback(User user, FeedbackDto feedbackDto) {
        log.info(START, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

        try {
            Feedback feedback = new Feedback();
            feedback.setUser(user);
            feedback.setGoalAchieved(feedbackDto.getGoalAchieved());
            feedback.setSatisfaction(feedbackDto.getSatisfaction());
            feedback.setSuggestions(feedbackDto.getSuggestions());
            feedbackRepository.save(feedback);
            log.info(END, Thread.currentThread().getStackTrace()[1].getMethodName(), this.getClass().getSimpleName());

            return true;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    SERVER_ERROR_IN + this.getClass().getSimpleName(), e);
        }
    }
}


