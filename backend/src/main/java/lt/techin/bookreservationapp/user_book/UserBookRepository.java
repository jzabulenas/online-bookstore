package lt.techin.bookreservationapp.user_book;

import java.util.List;
import java.util.Optional;
import lt.techin.bookreservationapp.book.Book;
import lt.techin.bookreservationapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserBookRepository extends JpaRepository<UserBook, Long> {

  Optional<UserBook> findByUserAndBook(User user, Book book);

  Optional<UserBook> findByUserIdAndBookTitle(long id, String title);

  @Query("SELECT ub.book.title FROM UserBook ub WHERE ub.user.email = :#{authentication.name}")
  List<String> findAllTitles();

  // I use this method in a test, so I am able to retrieve books belonging to a
  // user. I cannot use the `findAllTitles` method, because I am not able to
  // retrieve the Security context there
  @Query("SELECT ub.book.title FROM UserBook ub WHERE ub.user.email = ?1")
  List<String> findAllTitlesByEmail(String email);

  boolean existsByBookTitleAndUser(String title, User user);
}
