package club.petgo.petgousers.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "USER")
@NoArgsConstructor
public class User implements Serializable {

    public User(String email, String password, String userName) {
        this.email = email;
        this.password = password;
        this.userName = userName;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NonNull
    private String email;

    @NonNull
    private String password;

    @NonNull
    private String userName;

    @ElementCollection
    @CollectionTable(
            name = "ROLE",
            joinColumns = @JoinColumn(name = "USER_ID"))
    @Column(name = "ROLE_NAME")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
    }

    public enum  Role {
        USER,
        PET_OWNER,
        SERVICE_PROVIDER
    }
}
