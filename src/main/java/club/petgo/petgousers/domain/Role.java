package club.petgo.petgousers.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "role")
public class Role {

    @Id
    @Enumerated(EnumType.STRING)
    public RoleName roleName;

    @ManyToMany(mappedBy = "roles")
    Set<User> users = new HashSet<>();

    public enum  RoleName {
        USER,
        PET_OWNER,
        SERVICE_PROVIDER
    }
}
