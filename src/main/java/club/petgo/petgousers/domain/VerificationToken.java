package club.petgo.petgousers.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime expirationDateTime;

    @Transient
    private long expirationPeriodInDays;

    public VerificationToken(String token, User user, long expirationPeriodInDays) {
        this.token = token;
        this.user = user;
        this.expirationPeriodInDays = expirationPeriodInDays;
        this.expirationDateTime = calculateExpiration();
    }

    private LocalDateTime calculateExpiration() {
        return LocalDateTime.now().plusDays(expirationPeriodInDays);
    }
}
