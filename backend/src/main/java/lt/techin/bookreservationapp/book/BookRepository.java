package lt.techin.bookreservationapp.book;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

// TODO: remove comments
//  @Query("SELECT sb.title FROM Book sb WHERE sb.user.email = :#{authentication.name}")
//  List<String> findAllTitles();

//  boolean existsByTitleAndUser(String title, User user);

//  Optional<Book> findByTitleAndUser(String title, User user);

  Optional<Book> findByTitle(String title);

  boolean existsByTitle(String title);
}
