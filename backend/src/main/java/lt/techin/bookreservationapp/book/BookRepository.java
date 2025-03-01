package lt.techin.bookreservationapp.book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface BookRepository extends JpaRepository<SavedBook, Long> {

  @Query("SELECT sb.title FROM SavedBook sb WHERE sb.user.email = :#{authentication.name}")
  List<String> findAllTitles();
}
