package club.petgo.petgousers.domain.profile;

import club.petgo.petgousers.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    protected String firstName;
    protected String lastName;
    protected String phone;

    @Embedded
    protected Address address;

    @Embedded
    protected Image profilePicture;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    protected User user;

    public Profile(String firstName,
                   String lastName,
                   String phone,
                   Address address,
                   User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.user = user;
    }
}
