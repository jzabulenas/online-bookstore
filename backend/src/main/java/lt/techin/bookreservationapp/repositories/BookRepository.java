package lt.techin.bookreservationapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import lt.techin.bookreservationapp.entities.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

  boolean existsByIsbn(String isbn);

  boolean existsByTitle(String title);

  boolean existsById(int id);
}
