package lt.techin.bookreservationapp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lt.techin.bookreservationapp.entities.Book;
import lt.techin.bookreservationapp.entities.Category;
import lt.techin.bookreservationapp.repositories.BookRepository;
import lt.techin.bookreservationapp.repositories.CategoryRepository;

@CrossOrigin("http://localhost:3000")
@RestController
public class BookController {

  private final BookRepository bookRepository;

  private final CategoryRepository categoryRepository;

  @Autowired
  public BookController(BookRepository bookRepository, CategoryRepository categoryRepository) {
    this.bookRepository = bookRepository;
    this.categoryRepository = categoryRepository;
  }

  @GetMapping("/books")
  public ResponseEntity<?> getBooks() {
    List<Book> books = bookRepository.findAll();

    if (!books.isEmpty()) {
      return ResponseEntity.ok(books);
    } else {
      Map<String, String> response = new HashMap<>();
      response.put("message", "No books found");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
  }

  @GetMapping("/books/{id}")
  public ResponseEntity<Book> getBook(@PathVariable int id) {

    Optional<Book> book = bookRepository.findById(id);
    if (book.isPresent()) {
      return ResponseEntity.ok(book.get());
    }

    return ResponseEntity.notFound().build();
  }

  @PostMapping("/books")
  public ResponseEntity<String> addBook(@Valid @RequestBody Book book) {
      return ResponseEntity.badRequest().body("Title already exists");
    }

    if (bookRepository.existsByIsbn(book.getIsbn())) {
      return ResponseEntity.badRequest().body("ISBN already exists");
    }

    List<Category> categories = new ArrayList<>();
    Set<Integer> uniqueIds = new HashSet<>();

    for (Category category : book.getCategories()) {
      if (!uniqueIds.add(category.getId())) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categories cannot be duplicate");
      }

      Category existingCategory = categoryRepository.findById(category.getId()).get();
      categories.add(existingCategory);
    }

    if (categories.contains(null)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such categories exist!");
    }

    book.setCategories(categories);
    bookRepository.save(book);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
