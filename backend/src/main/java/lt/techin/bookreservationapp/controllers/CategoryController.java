package lt.techin.bookreservationapp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lt.techin.bookreservationapp.entities.Category;
import lt.techin.bookreservationapp.services.CategoryService;

// @CrossOrigin("http://localhost:3000")
@RestController
public class CategoryController {

  private final CategoryService categoryService;

  @Autowired
  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping("/categories")
  public ResponseEntity<List<Category>> getCategories() {
    return ResponseEntity.ok(categoryService.findAllCategories());
  }

  @GetMapping("/categories/{id}")
  public ResponseEntity<Category> getCategory(@PathVariable int id) {
    if (!categoryService.existsCategoryById(id)) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(categoryService.findCategoryById(id));
  }

  @PostMapping("/categories")
  public ResponseEntity<?> addCategory(@Valid @RequestBody Category category) {
    if (categoryService.existsCategoryByName(category.getName())) {
      Map<String, String> categoryMap = new HashMap<String, String>();
      categoryMap.put("name", "Category already exists");
      return ResponseEntity.badRequest().body(categoryMap);
    }

    Category saveCategory = categoryService.saveCategory(category);
    return ResponseEntity.status(HttpStatus.CREATED).body(saveCategory);
  }

  @PutMapping("/categories/{id}")
  public ResponseEntity<?> updateCategory(
      @PathVariable int id, @Valid @RequestBody Category category) {
    Category currentCategory = categoryService.findCategoryById(id);
    if (categoryService.existsCategoryByName(category.getName())) {
      Map<String, String> categoryMap = new HashMap<String, String>();
      categoryMap.put("name", "Category already exists");
      return ResponseEntity.badRequest().body(categoryMap);
    }

    if (currentCategory != null) {
      currentCategory.setName(category.getName());

      return ResponseEntity.ok(categoryService.saveCategory(currentCategory));
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.saveCategory(category));
  }

  @DeleteMapping("/categories/{id}")
  public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
    if (categoryService.existsCategoryById(id)) {
      categoryService.deleteCategoryById(id);

      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.notFound().build();
  }
}
