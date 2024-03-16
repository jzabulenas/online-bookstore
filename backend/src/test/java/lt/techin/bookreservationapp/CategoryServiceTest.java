package lt.techin.bookreservationapp;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;
import lt.techin.bookreservationapp.entities.Category;
import lt.techin.bookreservationapp.repositories.CategoryRepository;
import lt.techin.bookreservationapp.services.CategoryService;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
// To use application-test.properties file, for another database
@ActiveProfiles("test")
public class CategoryServiceTest {

  @Autowired private CategoryRepository categoryRepository;

  @Autowired private CategoryService categoryService;

  @Test
  public void findAll_savedCategories_areReturned() {
    Category savedCategory1 = categoryRepository.save(new Category("Business & Money"));
    Category savedCategory2 = categoryRepository.save(new Category("Mystery, Thriller & Suspense"));

    List<Category> categories = categoryService.findAll();

    then(categories.get(0).getName()).isEqualTo(savedCategory1.getName());
    then(categories.get(0).getId()).isNotEqualTo(0);
    then(categories.get(1).getName()).isEqualTo(savedCategory2.getName());
    then(categories.get(1).getId()).isNotEqualTo(0);
  }

  @Test
  public void findById_forSavedCategory_isReturned() {
    Category savedCategory = categoryRepository.save(new Category("Nonfiction"));

    Category category = categoryService.findById(savedCategory.getId());

    then(category.getName()).isEqualTo("Nonfiction");
    then(category.getId()).isNotEqualTo(0);
  }

  @Test
  public void existsByName_savedCategory_isTrue() {
    Category category = categoryRepository.save(new Category("Literature & Fiction"));

    boolean doesCategoryExist = categoryService.existsByName(category.getName());

    then(doesCategoryExist).isTrue();
  }

  @Test
  void save_savedCategory_isReturned() {
    categoryService.save(new Category("Biographies & Memoirs"));

    Category category = categoryRepository.findByName("Biographies & Memoirs");

    then(category.getName()).isEqualTo("Biographies & Memoirs");
    then(category.getId()).isNotEqualTo(0);
  }
}
