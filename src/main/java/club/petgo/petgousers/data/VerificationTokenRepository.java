package club.petgo.petgousers.data;

import club.petgo.petgousers.domain.User;
import club.petgo.petgousers.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    VerificationToken findByUser(User user);
}
