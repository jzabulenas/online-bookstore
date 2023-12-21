package lt.techin.bookreservationapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lt.techin.bookreservationapp.entities.User;
import lt.techin.bookreservationapp.repositories.UserRepository;

@RestController
@CrossOrigin("http://localhost:3000")
public class AuthController {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public AuthController(UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/signup")
	public void signup(@RequestBody User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("USER");

		userRepository.save(user);
	}

	@PostMapping("/login")
	public User login(@RequestBody User user) {
		User userDb = userRepository
				.findUserByUsername(user.getUsername())
				.filter(uDb -> passwordEncoder.matches(user.getPassword(),
						uDb.getPassword()))
				.get();

		return userDb;
	}
}
