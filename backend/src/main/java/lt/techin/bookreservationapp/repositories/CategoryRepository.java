package lt.techin.bookreservationapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import lt.techin.bookreservationapp.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

  boolean existsByName(String name);

  Category findByName(String name);
}
