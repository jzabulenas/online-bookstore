package lt.techin.bookreservationapp.book;

import java.security.Principal;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import lt.techin.bookreservationapp.user.User;
import lt.techin.bookreservationapp.user.UserRepository;

@RestController
public class BookController {

  private final ChatClient chatClient;
  private final BookRepository bookRepository;
  private final UserRepository userRepository;
  private final BookService bookService;

  BookController(
      ChatClient chatClient,
      BookRepository bookRepository,
      UserRepository userRepository,
      BookService bookService) {
    this.chatClient = chatClient;
    this.bookRepository = bookRepository;
    this.userRepository = userRepository;
    this.bookService = bookService;
  }

  @PostMapping("/generate-books")
  ResponseEntity<String> generateBooks(@RequestBody @Valid MessageRequestDTO messageRequestDTO) {
    return ResponseEntity.ok(this.bookService.generateBooks(messageRequestDTO));
  }

  @PostMapping("/save-book")
  ResponseEntity<Book> saveBook(
      @Valid @RequestBody BookRequestDTO bookRequestDTO, Principal principal) {
    User user =
        this.userRepository
            .findByEmail(principal.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    Book savedBook = new Book(bookRequestDTO.title(), user);
    this.bookRepository.save(savedBook);

    return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
  }

  @GetMapping("/books")
  ResponseEntity<List<String>> getBooks() {
    return ResponseEntity.ok(this.bookRepository.findAllTitles());
  }
}
