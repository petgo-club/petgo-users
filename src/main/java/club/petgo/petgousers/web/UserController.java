package club.petgo.petgousers.web;

import club.petgo.petgousers.data.RoleRepository;
import club.petgo.petgousers.data.UserRepository;
import club.petgo.petgousers.domain.Role;
import club.petgo.petgousers.domain.User;
import club.petgo.petgousers.transistory.UserRegistrationForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
public class UserController {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserController(PasswordEncoder passwordEncoder,
                          UserRepository userRepository,
                          RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/register")
    public User register(@RequestBody @Valid UserRegistrationForm form) {
        LOGGER.info("Registering new user [{}]", form.getUserName());
        User user =  userRepository.save(form.toUser(passwordEncoder));
        roleRepository.save(new Role(Role.RoleName.USER, user));
        return user;
    }
}
