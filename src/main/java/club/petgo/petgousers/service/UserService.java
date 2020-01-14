package club.petgo.petgousers.service;

import club.petgo.petgousers.data.RoleRepository;
import club.petgo.petgousers.data.UserRepository;
import club.petgo.petgousers.domain.Role;
import club.petgo.petgousers.domain.User;
import club.petgo.petgousers.transistory.UserRegistrationForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Value("${bound}")
    protected int bound;

    private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(UserRegistrationForm form) {
        User user =  userRepository.save(form.toUser(passwordEncoder, setDefaultUserName(form.getEmail())));
        roleRepository.save(new Role(Role.RoleName.USER, user));
        LOGGER.info("Registered new user [{}]", user.getId());
        return user;
    }

    protected String setDefaultUserName(String email) {
        String defaultUserNameStart = getDefaultUserNameStart(email);
        String defaultUsername = String.format("%s%s", defaultUserNameStart, getDefaultUserNameEnd());

        while (userRepository.existsByUserName(defaultUsername)) {
            defaultUsername = String.format("%s%s", defaultUserNameStart, getDefaultUserNameEnd());
        }

        return defaultUsername;
    }

    private String getDefaultUserNameStart(String email) {
        return email.substring(0, email.indexOf('@'));
    }

    private String getDefaultUserNameEnd() {
        Integer random = new Random().nextInt(bound);
        return random.toString();
    }
}
