package club.petgo.petgousers.service;

import club.petgo.petgousers.data.UserRepository;
import club.petgo.petgousers.data.VerificationTokenRepository;
import club.petgo.petgousers.domain.User;
import club.petgo.petgousers.transistory.UserRegistrationForm;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
    PasswordEncoder mockPasswordEncoder = Mockito.mock(PasswordEncoder.class);
    VerificationTokenRepository mockTokenRepository = Mockito.mock(VerificationTokenRepository.class);

    UserService userService = new UserService(
            mockUserRepository,
            mockPasswordEncoder,
            mockTokenRepository);

    UserRegistrationForm form = new UserRegistrationForm();

    @Test
    public void testRegisterNewUser() {
        form.setEmail("user@gmail.com");
        form.setPassword("password");
        userService.bound = 1000;
        userService.registerNewUser(form);

        verify(mockUserRepository, times(1)).save(any(User.class));
        assertEquals(1, userService.registerNewUser(form).getRoles().size());
        assertTrue(userService.registerNewUser(form).getRoles().contains(User.Role.USER));
    }

    @Test
    public void testSetDefaultUserName() {
        userService.bound = 1000;

        assertTrue(userService.setDefaultUserName("user@gmail.com").contains("user"));
        assertFalse(userService.setDefaultUserName("user@gmail.com").contains("@gmail.com"));
        assertEquals(7, userService.setDefaultUserName("user@gmail.com").length());
    }
}
