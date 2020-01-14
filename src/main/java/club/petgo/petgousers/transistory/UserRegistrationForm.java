package club.petgo.petgousers.transistory;

import club.petgo.petgousers.domain.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UserRegistrationForm {

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 5, max = 64)
    private String password;

    public User toUser(PasswordEncoder passwordEncoder, String defaultUserName) {
        return new User(
                this.email,
                passwordEncoder.encode(this.password),
                defaultUserName
        );
    }
}
