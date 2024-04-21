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
import lt.techin.bookreservationapp.services.BookService;
import lt.techin.bookreservationapp.services.CategoryService;

@CrossOrigin("http://localhost:3000")
@RestController
public class BookController {

  private final BookRepository bookRepository;

  private final CategoryService categoryService;

  private final BookService bookService;

  @Autowired
  public BookController(
      BookRepository bookRepository, CategoryService categoryService, BookService bookService) {
    this.bookRepository = bookRepository;
    this.categoryService = categoryService;
    this.bookService = bookService;
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
  public ResponseEntity<?> addBook(@Valid @RequestBody Book book) {
    Map<String, String> responseJson = new HashMap<String, String>();

    if (bookService.existsBookByTitle(book.getTitle())) {
      responseJson.put("title", "Already exists");
      return ResponseEntity.badRequest().body(responseJson);
    }

    if (bookService.existsBookByIsbn(book.getIsbn())) {
      responseJson.put("isbn", "Already exists");
      return ResponseEntity.badRequest().body(responseJson);
    }

    List<Category> categories = new ArrayList<>();
    Set<Integer> uniqueIds = new HashSet<>();

    for (Category category : book.getCategories()) {
      if (!uniqueIds.add(category.getId())) {
        responseJson.put("categories", "Cannot be duplicate");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseJson);
      }

      Category existingCategory = categoryService.findCategoryById(category.getId());
      categories.add(existingCategory);
    }

    if (categories.contains(null)) {
      responseJson.put("categories", "Does not exist");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseJson);
    }

    book.setCategories(categories);
    return ResponseEntity.status(HttpStatus.CREATED).body(bookRepository.save(book));
  }
}
