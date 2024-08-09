package lt.techin.bookreservationapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import lt.techin.bookreservationapp.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

  // Optional<User> findUserByUsername(String username);

  // boolean existsByUsername(String username);
}
