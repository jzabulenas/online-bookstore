package lt.techin.bookreservationapp.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  // Optional<User> findUserByUsername(String username);

  // boolean existsByUsername(String username);

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);
}
