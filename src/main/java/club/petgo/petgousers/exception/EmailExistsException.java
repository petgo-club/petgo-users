package club.petgo.petgousers.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Email already in use")
public class EmailExistsException extends Exception {

   public EmailExistsException(String message) {
       super(message);
   }
}
