package club.petgo.petgousers.web;

import club.petgo.petgousers.data.UserRepository;
import club.petgo.petgousers.domain.User;
import club.petgo.petgousers.domain.VerificationToken;
import club.petgo.petgousers.event.OnRegistrationCompleteEvent;
import club.petgo.petgousers.exception.EmailExistsException;
import club.petgo.petgousers.service.UserService;
import club.petgo.petgousers.transistory.UserRegistrationForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping(value = "/register", produces = "application/json")
    @ResponseBody
    public User register(@RequestBody @Valid UserRegistrationForm form) throws EmailExistsException {
        LOGGER.info("Processing new user [{}] registration request", form.getEmail());

        if(userRepository.existsByEmail(form.getEmail())) {
            throw new EmailExistsException(String.format("Email [%s] already in use", form.getEmail()));
        }

        User user = userService.registerNewUser(form);

        try {
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
        } catch (Exception e) {
            LOGGER.info("Could not send registration email to [{}]", form.getEmail());
        }

        LOGGER.info("Registration email sent to [{}]", form.getEmail());
        return user;
    }

    @GetMapping(value = "/registrationConfirm")
    public String confirmRegistration (@RequestParam("token") String token) {
        VerificationToken verificationToken = userService.getVerificationToken(token);

        if (verificationToken == null) {
            return messageSource.getMessage("registration.invalidToken", null, Locale.ENGLISH);
        }

        User user = verificationToken.getUser();

        if ((verificationToken.getExpirationDateTime().isBefore(LocalDateTime.now()))) {
            return messageSource.getMessage("registration.expiredToken", null, Locale.ENGLISH);
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        return "Account Activated!";
    }

    @GetMapping(value = "/hello")
    @ResponseBody
    public String hello() {
        return "Hello petgo!";
    }
}
