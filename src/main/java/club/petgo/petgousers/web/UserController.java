package club.petgo.petgousers.web;

import club.petgo.petgousers.data.UserRepository;
import club.petgo.petgousers.domain.User;
import club.petgo.petgousers.domain.VerificationToken;
import club.petgo.petgousers.event.OnRegistrationCompleteEvent;
import club.petgo.petgousers.exception.EmailExistsException;
import club.petgo.petgousers.service.FileStorageService;
import club.petgo.petgousers.service.UserService;
import club.petgo.petgousers.transistory.ProfileForm;
import club.petgo.petgousers.transistory.UserRegistrationForm;
import club.petgo.petgousers.transistory.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Locale;

@RestController
@RequestMapping("/v1")
public class UserController {

    private UserService userService;
    private UserRepository userRepository;
    private ApplicationEventPublisher eventPublisher;
    private MessageSource messageSource;
    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService,
                          UserRepository userRepository,
                          ApplicationEventPublisher eventPublisher,
                          MessageSource messageSource) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.messageSource = messageSource;
    }

    @PostMapping(value = "/registration", produces = "application/json")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRegistrationForm form)
            throws EmailExistsException {
        LOGGER.info("Processing new user [{}] registration request", form.getEmail());

        if (userRepository.existsByEmail(form.getEmail())) {
            throw new EmailExistsException();
        }

        User user = userService.registerNewUser(form);

        try {
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
        } catch (Exception e) {
            LOGGER.info("Could not send registration email to [{}]", form.getEmail());
        }

        UserResponse userResponse = new UserResponse(
                user.getUsername(),
                user.getEmail(),
                user.isEnabled());

        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @GetMapping(value = "/registrationConfirm")
    public ResponseEntity<String> confirmRegistration (@RequestParam("token") String token) {
        VerificationToken verificationToken = userService.getVerificationToken(token);

        if (verificationToken == null) {
            return new ResponseEntity<>(messageSource.getMessage("activation.invalidToken",
                    null, Locale.ENGLISH),
                    HttpStatus.OK);
        }

        User user = verificationToken.getUser();

        if ((verificationToken.getExpirationDateTime().isBefore(LocalDateTime.now()))) {
            return new ResponseEntity<>(messageSource.getMessage("activation.expiredToken",
                    null, Locale.ENGLISH),
                    HttpStatus.OK);
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        return new ResponseEntity<>(messageSource.getMessage("activation.success",
                null, Locale.ENGLISH),
                HttpStatus.OK);
    }

    @PostMapping(value = "/profileCreate")
    public void createProfile(@RequestBody ProfileForm profileForm, Principal principal)
            throws Exception {
        User user = userRepository.findByUserName(principal.getName());

        if(user == null) {
            throw new UserPrincipalNotFoundException(String.format("User [%s] not found", principal.getName()));
        }

        userService.createProfile(profileForm, user);
    }

    // Only for testing purposes, to be deleted later
    @GetMapping(value = "/hello")
    public String hello() {
        return "Hello petgo!";
    }

    @PostMapping(value = "/imageUpload")
    public void uploadImage(@RequestParam("image") MultipartFile image, Principal principal)
            throws Exception {
        User user = userRepository.findByUserName(principal.getName());

        if(user == null) {
            throw new UserPrincipalNotFoundException(String.format("User [%s] not found", principal.getName()));
        }

        userService.addProfilePicture(user, image);
    }
}
