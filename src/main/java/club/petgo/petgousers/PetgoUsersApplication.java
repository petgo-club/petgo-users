package club.petgo.petgousers;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class PetgoUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetgoUsersApplication.class, args);
	}
}
