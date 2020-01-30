package club.petgo.petgousers.service;

import club.petgo.petgousers.data.UserRepository;
import club.petgo.petgousers.data.VerificationTokenRepository;
import club.petgo.petgousers.domain.User;
import club.petgo.petgousers.domain.VerificationToken;
import club.petgo.petgousers.transistory.UserRegistrationForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService implements IUserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private VerificationTokenRepository tokenRepository;

    @Value("${bound}")
    protected int bound;

    @Value("${email.expiration}")
    private long expirationInDays;

    private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = verificationTokenRepository;
    }

    public User registerNewUser(UserRegistrationForm form) {
        User user = form.toUser(passwordEncoder, setDefaultUserName(form.getEmail()));
        user.addRole(User.Role.USER);
        userRepository.save(user);
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
        int random = new Random().nextInt(bound);
        return Integer.toString(random);
    }

    @Override
    public User getUserByToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken).getUser();
    }

    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(token, user, expirationInDays);
        tokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken getVerificationToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken);
    }
}
