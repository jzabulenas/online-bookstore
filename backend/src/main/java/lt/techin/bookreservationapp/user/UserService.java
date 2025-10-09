package lt.techin.bookreservationapp.user;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lt.techin.bookreservationapp.role.Role;
import lt.techin.bookreservationapp.role.RoleMapper;
import lt.techin.bookreservationapp.role.RoleRepository;

@Service
class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;

  UserService(
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder, EmailService emailService) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
  }

  UserResponseDTO saveUser(UserRequestDTO userRequestDTO) {
    if (this.userRepository.existsByEmail(userRequestDTO.email())) {
      throw new EmailAlreadyExistsException();
    }

    List<Role> toRoles = RoleMapper.toEntities(userRequestDTO.roles(), roleRepository);
    User toUser = UserMapper.toEntity(userRequestDTO, passwordEncoder, toRoles);
    User savedUser = this.userRepository.save(toUser);
    List<Long> toRolesIds = RoleMapper.toIds(savedUser);

    try {
      this.emailService.sendVerificationMail(savedUser);
    } catch (UserMailFailedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return UserMapper.toDTO(savedUser, toRolesIds);
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
