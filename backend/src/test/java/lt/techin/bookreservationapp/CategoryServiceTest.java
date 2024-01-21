package lt.techin.bookreservationapp;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import jakarta.transaction.Transactional;
import lt.techin.bookreservationapp.entities.Category;
import lt.techin.bookreservationapp.repositories.CategoryRepository;
import lt.techin.bookreservationapp.services.CategoryService;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
public class CategoryServiceTest {

	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private CategoryService categoryService;

	@Test
	public void findById_forSavedCategory_isReturned() {
		Category savedCategory = categoryRepository
				.save(new Category("Nonfiction"));

		Category category = categoryService.findById(savedCategory.getId());

		then(category.getName()).isEqualTo("Nonfiction");
		then(category.getId()).isNotNull();
	}
}
