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
		User user = new User();
		user.setUsername("tony");
		user.setPassword(passwordEncoder.encode("soprano"));
		user.setRole("ADMIN");

		if (!userRepository.existsByUsername(user.getUsername())) {
			userRepository.save(user);
		}
	}

}
