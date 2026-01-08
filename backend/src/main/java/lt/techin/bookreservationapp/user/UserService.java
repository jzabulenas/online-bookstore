package lt.techin.bookreservationapp.user;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lt.techin.bookreservationapp.role.Role;
import lt.techin.bookreservationapp.role.RoleMapper;
import lt.techin.bookreservationapp.role.RoleRepository;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;

  UserService(
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder,
      EmailService emailService) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
  }

  UserResponseDTO saveUser(UserRequestDTO userRequestDTO) {
    if (this.userRepository.existsByEmail(userRequestDTO.email())) {
      throw new EmailAlreadyExistsException();
    }

    List<Role> toRoles = RoleMapper.toEntities(userRequestDTO.roles(), this.roleRepository);
    User toUser = UserMapper.toEntity(userRequestDTO, this.passwordEncoder, toRoles);
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

  /**
   * Retrieves the user based on the unique verification code. This method is used to find the user
   * based on code that was delivered to an email address, for verification.
   *
   * @param code the verification code that uniquely identifies the user
   * @return User found based on that code
   */
  User findUserByVerificationCode(String code) {
    return this.userRepository.findByVerificationCode(code).orElseThrow();
  }

  public User findUserByEmail(String email) {
    return this.userRepository
        .findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
  }

  User saveUser(User user) {
    return this.userRepository.save(user);
  }
}
