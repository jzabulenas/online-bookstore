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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
public class BookController {
    private final BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/books")
    public List<Book> getBooks() {
        return bookRepository.findAll();
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

        if (bookRepository.existsByIsbn(book.getIsbn())) {
            System.out.println(book.getIsbn());
            return ResponseEntity.badRequest().body("ISBN already exists");
        }

        List<Category> categories = new ArrayList<>();
        for (Category category : book.getCategories()) {
            Category existingCategory = categoryRepository.findByName(category.getName());
            categories.add(existingCategory);

        }

        book.setCategories(categories);
        bookRepository.save(book);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
