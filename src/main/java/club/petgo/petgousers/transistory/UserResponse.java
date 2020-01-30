package club.petgo.petgousers.transistory;

import lombok.Data;

@Data
public class UserResponse {

    String userName;
    String email;
    boolean enabled;

    public UserResponse(String userName, String email, boolean enabled) {
        this.userName = userName;
        this.email = email;
        this.enabled = enabled;
    }
}
