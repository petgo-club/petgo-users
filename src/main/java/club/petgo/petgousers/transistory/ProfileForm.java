package club.petgo.petgousers.transistory;

import club.petgo.petgousers.domain.User;
import club.petgo.petgousers.domain.profile.Address;
import club.petgo.petgousers.domain.profile.Image;
import club.petgo.petgousers.domain.profile.Profile;
import club.petgo.petgousers.domain.profile.ServiceProviderProfile;
import lombok.Data;

@Data
public class ProfileForm {

    private Type type;
    private String firstName;
    private String lastName;
    private String phone;
    private Address address;
    private Image profilePicture;
    private int servicedDistance;
    private int hourlyFee;

    public Profile toPetOwner(User user) {
        return new Profile(
                this.firstName,
                this.lastName,
                this.phone,
                this.address,
                this.profilePicture,
                user);
    }

    public Profile toServiceProvider(User user) {
        return new ServiceProviderProfile(
                this.firstName,
                this.lastName,
                this.phone,
                this.address,
                this.profilePicture,
                user,
                this.servicedDistance,
                this.hourlyFee);
    }

    public Profile toPetOwnerAndServiceProvider(User user) {
        return new ServiceProviderProfile(
                this.firstName,
                this.lastName,
                this.phone,
                this.address,
                this.profilePicture,
                user,
                this.servicedDistance,
                this.hourlyFee);
    }

    public enum Type {
        PET_OWNER,
        SERVICE_PROVIDER,
        OWNER_PROVIDER
    }
}
