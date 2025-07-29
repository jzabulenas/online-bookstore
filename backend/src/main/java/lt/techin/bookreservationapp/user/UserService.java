package lt.techin.bookreservationapp.user;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lt.techin.bookreservationapp.role.Role;
import lt.techin.bookreservationapp.role.RoleRepository;

@Service
class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  UserService(
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  UserResponseDTO saveUser(UserRequestDTO userRequestDTO) {
    if (this.userRepository.existsByEmail(userRequestDTO.email())) {
      throw new EmailAlreadyExistsException();
    }

    List<Role> toRoles = userRequestDTO.roles()
        .stream()
        .map(r -> this.roleRepository.findById(r).orElseThrow())
        .toList();

    User toUser = new User(
        userRequestDTO.email(),
        this.passwordEncoder.encode(userRequestDTO.password()),
        toRoles,
        null);

    User savedUser = this.userRepository.save(toUser);

    List<Long> toRolesIds = savedUser.getRoles().stream().map(r -> r.getId()).toList();

    UserResponseDTO toDTO = new UserResponseDTO(savedUser.getId(), savedUser.getEmail(),
        toRolesIds);

    return toDTO;
  }

  boolean existsUserByEmail(String email) {
    return this.userRepository.existsByEmail(email);
  }

  // public User findUserByUsernameAndPassword(String username, String password) {
  // return userRepository
  // .findUserByUsername(username)
  // .filter(u -> passwordEncoder.matches(password, u.getPassword()))
  // .orElseThrow();
  // }

  // public boolean existsUserByUsername(String username) {
  // return userRepository.existsByUsername(username);
  // }
}
