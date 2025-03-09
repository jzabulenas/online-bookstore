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
  ResponseEntity<MessageResponseDTO> generateBooks(
      @RequestBody @Valid MessageRequestDTO messageRequestDTO) {
    return ResponseEntity.ok(this.bookService.generateBooks(messageRequestDTO));
  }

  @PostMapping("/save-book")
  ResponseEntity<BookResponseDTO> saveBook(
      @Valid @RequestBody BookRequestDTO bookRequestDTO, Principal principal) {
    User user =
        this.userRepository
            .findByEmail(principal.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    Book savedBook = this.bookRepository.save(new Book(bookRequestDTO.title(), user));

        .body(new BookResponseDTO(savedBook.getTitle(), savedBook.getUser().getId()));
  }

  @GetMapping("/books")
  ResponseEntity<List<String>> getBooks() {
    return ResponseEntity.ok(this.bookRepository.findAllTitles());
  }
}
