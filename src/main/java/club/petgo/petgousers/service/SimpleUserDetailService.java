package club.petgo.petgousers.service;

import club.petgo.petgousers.data.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SimpleUserDetailService implements UserDetailsService {

    UserRepository userRepository;

    public SimpleUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserDetails user = userRepository.findByUserName(userName);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with username [%s] not found", userName));
        }

        return user;
    }
}
