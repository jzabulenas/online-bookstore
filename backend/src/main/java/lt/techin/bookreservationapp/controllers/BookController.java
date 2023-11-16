package lt.techin.bookreservationapp.controllers;

import jakarta.validation.Valid;
import lt.techin.bookreservationapp.entities.Book;
import lt.techin.bookreservationapp.entities.Category;
import lt.techin.bookreservationapp.repositories.BookRepository;
import lt.techin.bookreservationapp.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No books found");
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
    public ResponseEntity<String> addBook(@Valid @RequestBody Book book, BindingResult bindingResult) {
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

        if (bookRepository.existsByTitle(book.getTitle())) {
            return ResponseEntity.badRequest().body("Title already exists");
        }

        if (bookRepository.existsByIsbn(book.getIsbn())) {
            return ResponseEntity.badRequest().body("ISBN already exists");
        }

        List<Category> categories = new ArrayList<>();
        Set<String> uniqueCategoryNames = new HashSet<>();

        for (Category category : book.getCategories()) {

            if (!uniqueCategoryNames.add(category.getName())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categories cannot be duplicated!");
            }

            Category existingCategory = categoryRepository.findByName(category.getName());
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
