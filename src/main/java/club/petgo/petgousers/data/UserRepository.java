package club.petgo.petgousers.data;

import club.petgo.petgousers.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);
    User findByUserName(String userName);
}
