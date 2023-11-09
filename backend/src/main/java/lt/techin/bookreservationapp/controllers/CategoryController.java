package lt.techin.bookreservationapp.controllers;

import jakarta.validation.Valid;
import lt.techin.bookreservationapp.entities.Category;
import lt.techin.bookreservationapp.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable int id) {

        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            return ResponseEntity.ok(category.get());
        }

        return ResponseEntity.notFound().build();

    }

    @PostMapping("/categories")
    public ResponseEntity<String> addCategory(@Valid @RequestBody Category category, BindingResult bindingResult) {
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

        if (categoryRepository.existsByName(category.getName())) {
            return ResponseEntity.badRequest().body("Category already exists");
        }

        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable int id, @Valid @RequestBody Category updatedCategory, BindingResult bindingResult) {
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
