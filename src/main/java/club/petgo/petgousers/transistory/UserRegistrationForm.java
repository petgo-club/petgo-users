package club.petgo.petgousers.transistory;

import club.petgo.petgousers.domain.Role;
import club.petgo.petgousers.domain.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserRegistrationForm {

    private String userName;
    private String password;

    public User toUser(PasswordEncoder passwordEncoder) {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return new User(
                this.userName,
                passwordEncoder.encode(this.password),
                roles
        );
    }
}
