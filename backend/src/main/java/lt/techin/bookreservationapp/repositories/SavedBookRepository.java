package lt.techin.bookreservationapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lt.techin.bookreservationapp.entities.SavedBook;

public interface SavedBookRepository extends JpaRepository<SavedBook, Long> {

  @Query("SELECT sb.title FROM SavedBook sb WHERE sb.user.email = :#{authentication.name}")
  List<String> findAllTitles();
}
