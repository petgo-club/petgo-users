package club.petgo.petgousers.service;

import club.petgo.petgousers.domain.User;
import club.petgo.petgousers.domain.VerificationToken;

public interface IUserService {

//    User registerNewUserAccount(UserDto accountDto)
//            throws EmailExistsException;
//
    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);
}
