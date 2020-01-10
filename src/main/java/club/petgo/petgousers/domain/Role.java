package club.petgo.petgousers.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "roles")
public class Role {

    public Role(RoleName roleName, User user) {
        this.roleName = roleName;
        this.user = user;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @ManyToOne(targetEntity = User.class)
    private User user;

    public enum  RoleName {
        USER,
        PET_OWNER,
        SERVICE_PROVIDER
    }
}
