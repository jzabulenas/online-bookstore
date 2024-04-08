package lt.techin.bookreservationapp.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lt.techin.bookreservationapp.entities.User;
import lt.techin.bookreservationapp.repositories.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User saveUser(User user) {
    return userRepository.save(user);
  }

  public User findUserByUsernameAndPassword(String username, String password) {
    return userRepository
        .findUserByUsername(username)
        .filter(u -> passwordEncoder.matches(password, u.getPassword()))
        .orElseThrow();
  }
}
