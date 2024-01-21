package lt.techin.bookreservationapp;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;

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

	@Test
	public void findAll_savedCategories_areReturned() {
		Category savedCategory1 = categoryRepository
				.save(new Category("Business & Money"));
		Category savedCategory2 = categoryRepository
				.save(new Category("Mystery, Thriller & Suspense"));

		List<Category> categories = categoryService.findAll();

		then(categories).contains(savedCategory1, savedCategory2);
	}
}
