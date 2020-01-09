package club.petgo.petgousers.web;

import club.petgo.petgousers.data.UserRepository;
import club.petgo.petgousers.domain.User;
import club.petgo.petgousers.transistory.UserRegistrationForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class UserController {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public UserController(PasswordEncoder passwordEncoder,
                          UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public User register(@RequestBody UserRegistrationForm form) {
        return userRepository.save(form.toUser(passwordEncoder));
    }
}
