package lt.techin.bookreservationapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lt.techin.bookreservationapp.entities.Category;
import lt.techin.bookreservationapp.repositories.CategoryRepository;
import lt.techin.bookreservationapp.services.CategoryService;
import lt.techin.bookreservationapp.services.ValidationService;

@CrossOrigin("http://localhost:3000")
@RestController
public class CategoryController {

	private final CategoryRepository categoryRepository;
	private final CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryRepository categoryRepository,
			CategoryService categoryService) {
		this.categoryRepository = categoryRepository;
		this.categoryService = categoryService;
	}

	@GetMapping("/categories")
	public ResponseEntity<List<Category>> getCategories() {
		List<Category> allCategories = categoryService.findAll();

		if (!allCategories.isEmpty()) {
			return ResponseEntity.ok(allCategories);
		}

		return ResponseEntity.notFound().build();
	}

	@GetMapping("/categories/{id}")
	public ResponseEntity<Category> getCategory(@PathVariable int id) {
		return ResponseEntity.ok(categoryService.findById(id));
	}

	@PostMapping("/categories")
	public ResponseEntity<?> addCategory(
			@Valid @RequestBody Category category,
			BindingResult bindingResult) {
		String errorResponse = ValidationService
				.processFieldErrors(bindingResult);

		if (errorResponse != null) {
			return ResponseEntity.badRequest().body(errorResponse);
		}

		if (categoryService.existsByName(category.getName())) {
			return ResponseEntity.badRequest().body("Category already exists");
		}

		Category savedCategory = categoryService.save(category);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
	}

//	@PutMapping("/categories/{id}")
//	public ResponseEntity<String> updateCategory(@PathVariable int id,
//			@Valid @RequestBody Category category,
//			BindingResult bindingResult) {
//		String errorResponse = ValidationService
//				.processFieldErrors(bindingResult);
//		if (errorResponse != null) {
//			return ResponseEntity.badRequest().body(errorResponse);
//		}
//
//		Optional<Category> currentCategory = categoryService.findById(id);
//
//		if (categoryService.existsByName(category.getName())) {
//			return ResponseEntity.badRequest().body("Category already exists");
//		}
//
//		if (currentCategory.isPresent()) {
//			currentCategory.get().setName(category.getName());
//			categoryService.save(currentCategory.get());
//
//			return ResponseEntity.ok().build();
//		}
//
//		categoryService.save(category);
//		return ResponseEntity.status(HttpStatus.CREATED).build();
//	}

	@DeleteMapping("/categories/{id}")
	public void deleteCategory(@PathVariable int id) {
		categoryRepository.deleteById(id);
	}

}
