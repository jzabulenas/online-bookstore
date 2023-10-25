package lt.techin.bookreservationapp.repositories;

import lt.techin.bookreservationapp.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
