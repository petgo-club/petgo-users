package club.petgo.petgousers.service;

import club.petgo.petgousers.data.RoleRepository;
import club.petgo.petgousers.data.UserRepository;
import club.petgo.petgousers.domain.Role;
import club.petgo.petgousers.domain.User;
import club.petgo.petgousers.transistory.UserRegistrationForm;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
    RoleRepository mockRoleRepository = Mockito.mock(RoleRepository.class);
    PasswordEncoder mockPasswordEncoder = Mockito.mock(PasswordEncoder.class);

    UserService userService = new UserService(mockUserRepository,
            mockRoleRepository,
            mockPasswordEncoder);

    UserRegistrationForm form = new UserRegistrationForm();

    @Test
    public void testRegisterNewUser() {
        form.setEmail("user@gmail.com");
        form.setPassword("password");
        userService.bound = 1000;
        doReturn(new User()).when(mockUserRepository).save(any(User.class));
        User user = userService.registerNewUser(form);

        verify(mockUserRepository, times(1)).save(any(User.class));
        verify(mockRoleRepository, times(1)).save(any(Role.class));

        /*
         * Tests that default username is set correctly
         */
        assertTrue(user.getUserName().contains(form.getEmail().substring(0, form.getEmail().indexOf('@'))));
        assertEquals(form.getEmail().substring(0, form.getEmail().indexOf('@')).length() + 3,
                user.getUserName().length());
    }
}
