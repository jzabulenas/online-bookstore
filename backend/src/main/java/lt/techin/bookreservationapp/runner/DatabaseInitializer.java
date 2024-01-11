package lt.techin.bookreservationapp.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lt.techin.bookreservationapp.entities.User;
import lt.techin.bookreservationapp.repositories.UserRepository;

@Component
public class DatabaseInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public DatabaseInitializer(UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) throws Exception {
//		Create admin
		User admin = new User();
		admin.setUsername("tony");
		admin.setPassword(passwordEncoder.encode("soprano"));
		admin.setRole("ADMIN");

		if (!userRepository.existsByUsername(admin.getUsername())) {
			userRepository.save(admin);
		}
	}

}
