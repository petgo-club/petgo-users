package club.petgo.petgousers.domain.profile;

import club.petgo.petgousers.domain.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Data
@Entity
@PrimaryKeyJoinColumn(name = "Profile_id")
public class ServiceProviderProfile extends Profile {

    private int servicedDistance;
    private int hourlyFee;

    public ServiceProviderProfile(String firstName,
                                  String lastName,
                                  String phone,
                                  Address address,
                                  Image profilePicture,
                                  User user,
                                  int servicedDistance,
                                  int hourlyFee) {
        super(firstName, lastName, phone, address, profilePicture, user);
        this.hourlyFee = hourlyFee;
        this.servicedDistance = servicedDistance;
    }
}
