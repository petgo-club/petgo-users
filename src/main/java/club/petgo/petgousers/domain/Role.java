package club.petgo.petgousers.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
public class Role {

    @Id
    @NotEmpty
    private RoleName roleName;

    public enum  RoleName {
        USER,
        PET_OWNER,
        SERVICE_PROVIDER
    }
}
