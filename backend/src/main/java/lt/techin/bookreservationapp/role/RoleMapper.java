package lt.techin.bookreservationapp.role;

import java.util.List;
import lt.techin.bookreservationapp.user.User;

public class RoleMapper {

  public static List<Role> toEntities(List<Long> roleIds, RoleRepository roleRepository) {
    return roleIds.stream().map(r -> roleRepository.findById(r).orElseThrow()).toList();
  }

  public static List<Long> toIds(User user) {
    return user.getRoles().stream().map(r -> r.getId()).toList();
  }
}
