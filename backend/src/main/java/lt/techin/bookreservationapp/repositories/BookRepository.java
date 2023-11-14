package lt.techin.bookreservationapp.repositories;

import lt.techin.bookreservationapp.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Integer> {
    Boolean existsByIsbn(String name);
}