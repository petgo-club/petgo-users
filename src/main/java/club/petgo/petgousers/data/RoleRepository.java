package club.petgo.petgousers.data;

import club.petgo.petgousers.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
