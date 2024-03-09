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

	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	public Category findById(int id) {
		return categoryRepository.findById(id).orElse(null);
	}

	public boolean existsByName(String category) {
		return categoryRepository.existsByName(category);
	}

	public Category save(Category category) {
		return categoryRepository.save(category);
	}

	public boolean existsCategoryById(int id) {
		return categoryRepository.existsById(id);
	}

	public void deleteCategoryById(int id) {
		categoryRepository.deleteById(id);
	}

}
