package club.petgo.petgousers;

import club.petgo.petgousers.properties.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class PetgoUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetgoUsersApplication.class, args);
	}
}
