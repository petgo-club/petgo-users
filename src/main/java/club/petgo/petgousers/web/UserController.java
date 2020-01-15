package club.petgo.petgousers.web;

import club.petgo.petgousers.data.UserRepository;
import club.petgo.petgousers.domain.User;
import club.petgo.petgousers.exception.EmailExistsException;
import club.petgo.petgousers.service.UserService;
import club.petgo.petgousers.transistory.UserRegistrationForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
public class UserController {

    private UserService userService;
    private UserRepository userRepository;
    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService,
                          UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/register", produces = "application/json")
    @ResponseBody
    public User register(@RequestBody @Valid UserRegistrationForm form) throws EmailExistsException {
        LOGGER.info("Processing new user [{}] registration request", form.getEmail());

        if(userRepository.existsByEmail(form.getEmail())) {
            throw new EmailExistsException();
        }

        return userService.registerNewUser(form);
    }

    @GetMapping(value = "/hello")
    @ResponseBody
    public String hello() {
        return "Hello petgo!";
    }
}
