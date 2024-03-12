package lt.techin.bookreservationapp.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lt.techin.bookreservationapp.entities.Category;
import lt.techin.bookreservationapp.repositories.CategoryRepository;
import lt.techin.bookreservationapp.services.CategoryService;

@CrossOrigin("http://localhost:3000")
@RestController
public class CategoryController {

	private final CategoryRepository categoryRepository;

	private final CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryRepository categoryRepository, CategoryService categoryService) {
		this.categoryRepository = categoryRepository;
		this.categoryService = categoryService;
	}

	@GetMapping("/categories")
	public ResponseEntity<List<Category>> getCategories() {
		List<Category> allCategories = categoryService.findAll();

		if (!allCategories.isEmpty()) {
			return ResponseEntity.ok(allCategories);
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
	}

	@GetMapping("/categories/{id}")
	public ResponseEntity<Category> getCategory(@PathVariable int id) {
		if (!categoryService.existsCategoryById(id)) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(categoryService.findById(id));
	}

	@PostMapping("/categories")
	public ResponseEntity<?> addCategory(@Valid @RequestBody Category category) {
		if (categoryService.existsByName(category.getName())) {
			Map<String, String> categoryMap = new HashMap<String, String>();
			categoryMap.put("name", "Category already exists");
			return ResponseEntity.badRequest().body(categoryMap);
		}

		Category savedCategory = categoryService.save(category);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
	}

	@PutMapping("/categories/{id}")
	public ResponseEntity<?> updateCategory(@PathVariable int id, @Valid @RequestBody Category category) {
		Category currentCategory = categoryService.findById(id);
		if (categoryService.existsByName(category.getName())) {
			Map<String, String> categoryMap = new HashMap<String, String>();
			categoryMap.put("name", "Category already exists");
			return ResponseEntity.badRequest().body(categoryMap);
		}

		if (currentCategory != null) {
			currentCategory.setName(category.getName());

			return ResponseEntity.ok(categoryService.save(currentCategory));
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(category));
	}

	@DeleteMapping("/categories/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
		if (categoryService.existsCategoryById(id)) {
			categoryService.deleteCategoryById(id);

			return ResponseEntity.ok().build();
		}

		return ResponseEntity.notFound().build();
	}

}
