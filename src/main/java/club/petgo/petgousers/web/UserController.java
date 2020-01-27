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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Locale;

@RestController
@RequestMapping("/v1")
public class UserController {

    private UserService userService;
    private UserRepository userRepository;
    private ApplicationEventPublisher eventPublisher;
    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService,
                          UserRepository userRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping(value = "/register", produces = "application/json")
    @ResponseBody
    public User register(@RequestBody @Valid UserRegistrationForm form,
                         WebRequest request) throws EmailExistsException {
        LOGGER.info("Processing new user [{}] registration request", form.getEmail());

        if(userRepository.existsByEmail(form.getEmail())) {
            throw new EmailExistsException(String.format("Email [%s] already in use", form.getEmail()));
        }

        User user = userService.registerNewUser(form);

        try {
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(
                    user, request.getLocale(), appUrl
            ));
        } catch (Exception ignored) { }
        return user;
    }

    @GetMapping(value = "/registrationConfirm")
    public String confirmRegistration (WebRequest request, @RequestParam("token") String token) {

        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
//            String message = messages.getMessage("auth.message.invalidToken", null, locale);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
//            String messageValue = messages.getMessage("auth.message.expired", null, locale)
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
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
