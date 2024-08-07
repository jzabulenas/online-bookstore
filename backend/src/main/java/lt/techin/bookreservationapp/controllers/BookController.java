package lt.techin.bookreservationapp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import lt.techin.bookreservationapp.services.BookService;
import lt.techin.bookreservationapp.services.CategoryService;

@CrossOrigin("http://localhost:5173")
@RestController
public class BookController {

  private final CategoryService categoryService;

  private final BookService bookService;

  @Autowired
  public BookController(CategoryService categoryService, BookService bookService) {
    this.categoryService = categoryService;
    this.bookService = bookService;
  }

  @GetMapping("/books")
  public ResponseEntity<?> getBooks() {
    List<Book> books = bookService.findAllBooks();

    if (!books.isEmpty()) {
      return ResponseEntity.ok(books);
    } else {
      Map<String, String> response = new HashMap<>();
      response.put("message", "No books found");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
  }

  @GetMapping("/books/{id}")
  public ResponseEntity<?> getBook(@PathVariable int id) {
    Book book = bookService.findBookById(id);

    if (book == null) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "Book with Id " + id + " not found");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    return ResponseEntity.ok(book);
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

    for (Category category : book.getCategories()) {
      if (!categoryService.existsCategoryById(category.getId())) {
        responseJson.put("message", "Category with id of " + category.getId() + " does not exist");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseJson);
      }
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(bookService.saveBook(book));
  }
}
