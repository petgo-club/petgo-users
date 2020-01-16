package club.petgo.petgousers.service;

import club.petgo.petgousers.data.RoleRepository;
import club.petgo.petgousers.data.UserRepository;
import club.petgo.petgousers.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SimpleUserDetailService implements UserDetailsService {

    UserRepository userRepository;
    RoleRepository roleRepository;

    public SimpleUserDetailService(UserRepository userRepository,
                                   RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName);

        if (user == null) {
            throw new UsernameNotFoundException(
                    "No user found with username: "+ userName);
        }

        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        List<String> roles = roleRepository.findAllByUserId(user.getId())
                .stream()
                .map(role -> role.getRoleName().toString())
                .collect(Collectors.toList());
        return  new org.springframework.security.core.userdetails.User
                (user.getUserName().toLowerCase(),
                        user.getPassword().toLowerCase(), enabled, accountNonExpired,
                        credentialsNonExpired, accountNonLocked,
                        getAuthorities(roles));
    }

    private static List<GrantedAuthority> getAuthorities (List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
