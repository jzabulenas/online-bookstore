package lt.techin.bookreservationapp.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
	public List<Category> getCategories() {
		return categoryService.findAll();
	}

	@GetMapping("/categories/{id}")
	public ResponseEntity<Category> getCategory(@PathVariable int id) {
		return ResponseEntity.of(categoryService.findById(id));
	}

	@PostMapping("/categories")
	public ResponseEntity<String> addCategory(
			@Valid @RequestBody Category category,
			BindingResult bindingResult) {
		ResponseEntity<String> errorResponse = ValidationService
				.processFieldErrors(bindingResult);
		if (errorResponse != null) {
			return errorResponse;
		}

		if (categoryRepository.existsByName(category.getName())) {
			return ResponseEntity.badRequest().body("Category already exists");
		}

		categoryRepository.save(category);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/categories/{id}")
	public ResponseEntity<String> updateCategory(@PathVariable int id,
			@Valid @RequestBody Category updatedCategory,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			StringBuilder errorMessageBuilder = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();

			for (int i = 0; i < fieldErrors.size(); i++) {
				FieldError fieldError = fieldErrors.get(i);
				errorMessageBuilder.append(fieldError.getDefaultMessage());
				if (i < fieldErrors.size() - 1) {
					errorMessageBuilder.append(" | ");
				}
			}

			String errorMessage = errorMessageBuilder.toString();
			return ResponseEntity.badRequest().body(errorMessage);
		}

		Optional<Category> categoryFromDb = categoryRepository.findById(id);

		if (categoryRepository.existsByName(updatedCategory.getName())) {
			return ResponseEntity.badRequest().body("Category already exists");
		}

		if (categoryFromDb.isPresent()) {
			Category category = categoryFromDb.get();

			category.setName(updatedCategory.getName());
			categoryRepository.save(category);
			return ResponseEntity.ok().build();
		}

		categoryRepository.save(updatedCategory);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/categories/{id}")
	public void deleteCategory(@PathVariable int id) {
		categoryRepository.deleteById(id);
	}

}
