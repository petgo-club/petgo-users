package club.petgo.petgousers.data;

import club.petgo.petgousers.domain.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    boolean existsByUserId(UUID userId);
}
