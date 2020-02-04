package club.petgo.petgousers.domain.profile;

import club.petgo.petgousers.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Data
@Entity
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "Profile_id")
public class ServiceProviderProfile extends Profile {

    private int servicedDistance;
    private int hourlyFee;

    public ServiceProviderProfile(String firstName,
                                  String lastName,
                                  String phone,
                                  Address address,
                                  User user,
                                  int servicedDistance,
                                  int hourlyFee) {
        super(firstName, lastName, phone, address, user);
        this.hourlyFee = hourlyFee;
        this.servicedDistance = servicedDistance;
    }
}
