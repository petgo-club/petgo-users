package club.petgo.petgousers.service;

import club.petgo.petgousers.data.ProfileRepository;
import club.petgo.petgousers.data.UserRepository;
import club.petgo.petgousers.data.VerificationTokenRepository;
import club.petgo.petgousers.domain.User;
import club.petgo.petgousers.domain.VerificationToken;
import club.petgo.petgousers.domain.profile.Image;
import club.petgo.petgousers.domain.profile.Profile;
import club.petgo.petgousers.transistory.ProfileForm;
import club.petgo.petgousers.transistory.UserRegistrationForm;
import net.bytebuddy.implementation.bytecode.Throw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Random;

@Service
public class UserService implements IUserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private VerificationTokenRepository tokenRepository;
    private ProfileRepository profileRepository;
    private FileStorageService fileStorageService;

    @Value("${bound}")
    protected int bound;

    @Value("${email.expiration}")
    private long expirationInDays;

    private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       VerificationTokenRepository verificationTokenRepository,
                       ProfileRepository profileRepository,
                       FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = verificationTokenRepository;
        this.profileRepository = profileRepository;
        this.fileStorageService= fileStorageService;
    }

    public User registerNewUser(UserRegistrationForm form) {
        User user = form.toUser(passwordEncoder, setDefaultUserName(form.getEmail()));
        user.addRole(User.Role.USER);
        userRepository.save(user);
        LOGGER.info("Registered new user [{}]", user.getId());
        return user;
    }

    public void createProfile(ProfileForm profileForm, User user) throws Exception {

        if (profileRepository.existsByUserId(user.getId())) {
            throw new Exception("Profile already exists");
        }

        Profile profile = new Profile();

        switch (profileForm.getType()) {
            case PET_OWNER:
                user.addRole(User.Role.PET_OWNER);
                profile = profileForm.toPetOwner(user);
                break;
            case SERVICE_PROVIDER:
                user.addRole(User.Role.SERVICE_PROVIDER);
                profile = profileForm.toServiceProvider(user);
                break;
            case OWNER_PROVIDER:
                user.addRole(User.Role.PET_OWNER);
                user.addRole(User.Role.SERVICE_PROVIDER);
                profile = profileForm.toPetOwnerAndServiceProvider(user);
                break;
        }

        profileRepository.save(profile);
        userRepository.save(user);
    }

    public void addProfilePicture(User user, MultipartFile image) throws Exception {
        Profile profile = profileRepository.findByUserId(user.getId());

        if (profile == null) {
            throw new Exception("Profile not found");
        }

        if (!isImageFormatValid(image.getOriginalFilename())) {
            throw new Exception("Invalid image format");
        }

        String imageName = setImageName(image.getOriginalFilename(), user.getId().toString());
        String imageLocation = fileStorageService.storeFile(image, imageName);
        String imageFormat = imageName.substring(imageName.lastIndexOf('.') + 1).toUpperCase();
        profile.setProfilePicture(new Image(imageName, imageLocation, imageFormat));
        profileRepository.save(profile);
    }


    private boolean isImageFormatValid(String imageName) {
        return Arrays.stream(Image.ImageFormat.values())
                .anyMatch((format) -> format
                        .name()
                        .equals(imageName.substring(imageName.lastIndexOf('.') + 1).toUpperCase()));
    }

    private String setImageName(String fileName, String userId) {
        return  fileName.substring(0, fileName.lastIndexOf('.')) +
                userId +
                fileName.substring(fileName.lastIndexOf('.'));
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
