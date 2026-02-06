package lt.techin.bookreservationapp.user;

import java.util.List;
import lt.techin.bookreservationapp.role.Role;
import lt.techin.bookreservationapp.role.RoleMapper;
import lt.techin.bookreservationapp.role.RoleRepository;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final CompromisedPasswordChecker compromisedPasswordChecker;

  UserService(
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder,
      EmailService emailService,
      CompromisedPasswordChecker compromisedPasswordChecker) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
    this.compromisedPasswordChecker = compromisedPasswordChecker;
  }

  UserResponseDTO saveUser(UserRequestDTO userRequestDTO) {
    if (this.userRepository.existsByEmail(userRequestDTO.email())) {
      throw new EmailAlreadyExistsException();
    }

    if (this.compromisedPasswordChecker.check(userRequestDTO.password()).isCompromised()) {
      throw new CompromisedPasswordException(
          "The provided password is compromised and cannot be used. Use something more unique");
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
