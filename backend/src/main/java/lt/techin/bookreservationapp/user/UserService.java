package lt.techin.bookreservationapp.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  User saveUser(User user) {
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
