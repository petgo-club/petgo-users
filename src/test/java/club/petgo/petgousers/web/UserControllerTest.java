package club.petgo.petgousers.web;

import club.petgo.petgousers.data.UserRepository;
import club.petgo.petgousers.exception.EmailExistsException;
import club.petgo.petgousers.service.UserService;
import club.petgo.petgousers.transistory.UserRegistrationForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserRepository mockBeanUserRepository;

    @MockBean
    UserService mockBeanUserService;

    UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
    UserService mockUserService = Mockito.mock(UserService.class);
    ApplicationEventPublisher applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
    MessageSource messageSource = Mockito.mock(MessageSource.class);
    UserController userController = new UserController(mockUserService,
            mockUserRepository,
            applicationEventPublisher,
            messageSource);
    UserRegistrationForm form = new UserRegistrationForm();

    @Test
    public void testRegisterWithValidRequest() throws Exception {
        form.setEmail("user@gmail.com");
        form.setPassword("password");

        mockMvc.perform(post("/v1/register")
                .content(objectMapper.writeValueAsString(form))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockBeanUserRepository, times(1)).existsByEmail(anyString());
        verify(mockBeanUserService, times(1)).registerNewUser(any(UserRegistrationForm.class));
    }

    @Test
    public void testRegisterWithInvalidRequest() throws Exception {
        form.setEmail("invalid");
        form.setPassword("tiny");

        mockMvc.perform(post("/v1/register")
                .content(objectMapper.writeValueAsString(form))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testRegisterWithExistingEmail() {
        form.setEmail("user@gmail.com");
        doReturn(true).when(mockUserRepository).existsByEmail(anyString());
        Exception exception = assertThrows(EmailExistsException.class, () ->
            userController.register(form)
        );
        assertEquals("Email already in use", exception.getMessage());
    }
}
