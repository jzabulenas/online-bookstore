package lt.techin.bookreservationapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lt.techin.bookreservationapp.entities.Category;
import lt.techin.bookreservationapp.repositories.CategoryRepository;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<Category> getCategories() {
		return categoryRepository.findAll();
	}

	public Optional<Category> getCategory(int id) {
		return categoryRepository.findById(id);
	}
}
