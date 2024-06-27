package lt.techin.bookreservationapp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import lt.techin.bookreservationapp.entities.Category;
import lt.techin.bookreservationapp.repositories.CategoryRepository;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public List<Category> findAllCategories() {
    return categoryRepository.findAll();
  }

  public Category findCategoryById(long id) {
    return categoryRepository.findById(id).orElse(null);
  }

  public boolean existsCategoryByName(String category) {
    return categoryRepository.existsByName(category);
  }

  public Category saveCategory(Category category) {
    return categoryRepository.save(category);
  }

  public boolean existsCategoryById(long id) {
    return categoryRepository.existsById(id);
  }

  public void deleteCategoryById(long id) {
    categoryRepository.deleteById(id);
  }

  public Category findCategoryByName(String name) {
    return categoryRepository.findByName(name);
  }

  public void deleteAllCategories() {
    categoryRepository.deleteAll();
  }
}
