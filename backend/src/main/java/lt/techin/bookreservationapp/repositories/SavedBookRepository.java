package lt.techin.bookreservationapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import lt.techin.bookreservationapp.entities.SavedBook;

public interface SavedBookRepository extends JpaRepository<SavedBook, Long> {}
