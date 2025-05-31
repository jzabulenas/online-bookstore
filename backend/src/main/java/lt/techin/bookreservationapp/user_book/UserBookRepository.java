package lt.techin.bookreservationapp.user_book;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lt.techin.bookreservationapp.book.Book;
import lt.techin.bookreservationapp.user.User;

public interface UserBookRepository extends JpaRepository<UserBook, Long> {

  Optional<UserBook> findByUserAndBook(User user, Book book);

  Optional<UserBook> findByUserIdAndBookTitle(long id, String title);

  @Query("SELECT ub.book.title FROM UserBook ub WHERE ub.user.email = :#{authentication.name}")
  List<String> findAllTitles();
}
