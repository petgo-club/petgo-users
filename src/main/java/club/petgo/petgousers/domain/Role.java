package club.petgo.petgousers.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "role")
public class Role {

    public Role(RoleName roleName, User user) {
        this.roleName = roleName;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
