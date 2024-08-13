package lt.techin.bookreservationapp.services;

import org.springframework.stereotype.Service;

import lt.techin.bookreservationapp.entities.User;
import lt.techin.bookreservationapp.repositories.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User saveUser(User user) {
    return userRepository.save(user);
  }

  //  public User findUserByUsernameAndPassword(String username, String password) {
  //    return userRepository
  //        .findUserByUsername(username)
  //        .filter(u -> passwordEncoder.matches(password, u.getPassword()))
  //        .orElseThrow();
  //  }

  //  public boolean existsUserByUsername(String username) {
  //    return userRepository.existsByUsername(username);
  //  }
}
