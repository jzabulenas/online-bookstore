package lt.techin.bookreservationapp.user;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lt.techin.bookreservationapp.role.Role;
import lt.techin.bookreservationapp.role.RoleMapper;
import lt.techin.bookreservationapp.role.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

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
    CompromisedPasswordChecker compromisedPasswordChecker
  ) {
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

    if (
      this.compromisedPasswordChecker.check(
        userRequestDTO.password()
      ).isCompromised()
    ) {
      throw new CompromisedPasswordException(
        "The provided password is compromised and cannot be used. Use something more unique"
      );
    }

    Role userRole = this.roleRepository.findByName("ROLE_USER").orElseThrow();
    List<Role> toRoles = List.of(userRole);
    User toUser = UserMapper.toEntity(
      userRequestDTO,
      this.passwordEncoder,
      toRoles
    );
    User savedUser = this.userRepository.save(toUser);
    List<Long> toRolesIds = RoleMapper.toIds(savedUser);

    try {
      this.emailService.sendVerificationMail(savedUser);
    } catch (UserMailFailedException e) {
      LOG.error(
        "Failed to send verification email to {}",
        savedUser.getEmail(),
        e
      );
    }

    return UserMapper.toDTO(savedUser, toRolesIds);
  }

  boolean existsUserByEmail(String email) {
    return this.userRepository.existsByEmail(email);
  }

  /**
   * Retrieves the user based on the unique verification code. This method is used
   * to find the user
   * based on code that was delivered to an email address, for verification.
   *
   * @param code
   *               the verification code that uniquely identifies the user
   * @return User found based on that code
   */
  User findUserByVerificationCode(String code) {
    return this.userRepository.findByVerificationCode(code).orElseThrow();
  }

  public User findUserByEmail(String email) {
    return this.userRepository.findByEmail(email).orElseThrow(() ->
      new UsernameNotFoundException("User not found: " + email)
    );
  }

  void saveUser(User user) {
    this.userRepository.save(user);
  }

  void initiatePasswordReset(String email) {
    Optional<User> userOpt = this.userRepository.findByEmail(email);

    if (userOpt.isEmpty()) {
      return;
    }

    User user = userOpt.get();
    String token = UUID.randomUUID().toString().replace("-", "");
    user.setPasswordResetCode(token);
    user.setPasswordResetExpiry(
      LocalDateTime.now(Clock.systemUTC()).plusHours(1)
    );
    this.userRepository.save(user);

    try {
      this.emailService.sendPasswordResetMail(user);
    } catch (UserMailFailedException e) {
      LOG.error(
        "Failed to send password reset email to {}",
        user.getEmail(),
        e
      );
    }
  }

  void resetPassword(String token, String newPassword) {
    User user = this.userRepository.findByPasswordResetCode(token).orElseThrow(
      InvalidPasswordResetTokenException::new
    );

    if (
      user.getPasswordResetExpiry() == null ||
      LocalDateTime.now(Clock.systemUTC()).isAfter(
        user.getPasswordResetExpiry()
      )
    ) {
      throw new InvalidPasswordResetTokenException();
    }

    if (this.compromisedPasswordChecker.check(newPassword).isCompromised()) {
      throw new CompromisedPasswordException(
        "The provided password is compromised and cannot be used. Use something more unique"
      );
    }

    user.setPassword(this.passwordEncoder.encode(newPassword));
    user.clearPasswordResetToken();
    this.userRepository.save(user);
  }
}
