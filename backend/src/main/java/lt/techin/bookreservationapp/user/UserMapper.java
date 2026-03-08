package lt.techin.bookreservationapp.user;

import java.util.List;
import lt.techin.bookreservationapp.role.Role;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserMapper {

  static User toEntity(
      UserRequestDTO userRequestDTO, PasswordEncoder passwordEncoder, List<Role> roles) {
    return new User(
        userRequestDTO.email(),
        passwordEncoder.encode(userRequestDTO.password()),
        false,
        RandomStringUtils.randomAlphanumeric(32),
        roles,
        null);
  }

  static UserResponseDTO toDTO(User user, List<Long> toRolesIds) {
    return new UserResponseDTO(user.getId(), user.getEmail(), toRolesIds);
  }
}
