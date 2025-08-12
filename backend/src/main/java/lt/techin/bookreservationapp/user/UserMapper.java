package lt.techin.bookreservationapp.user;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

import lt.techin.bookreservationapp.role.Role;

public class UserMapper {

  public static User toEntity(UserRequestDTO userRequestDTO, PasswordEncoder passwordEncoder,
      List<Role> roles) {
    return new User(
        userRequestDTO.email(),
        passwordEncoder.encode(userRequestDTO.password()),
        roles,
        null);
  }

  public static UserResponseDTO toDTO(User user, List<Long> toRolesIds) {
    return new UserResponseDTO(
        user.getId(),
        user.getEmail(),
        toRolesIds);
  }
}
