package lt.techin.bookreservationapp.book;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lt.techin.bookreservationapp.user.User;

public interface BookRepository extends JpaRepository<Book, Long> {

  @Query("SELECT sb.title FROM Book sb WHERE sb.user.email = :#{authentication.name}")
  List<String> findAllTitles();

  boolean existsByTitleAndUser(String title, User user);

  Optional<Book> findByTitleAndUser(String title, User user);
}
