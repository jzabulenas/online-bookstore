package lt.techin.bookreservationapp.book;

import java.security.Principal;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lt.techin.bookreservationapp.rate_limiting.WithRateLimitProtection;
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
  @WithRateLimitProtection
  ResponseEntity<MessageResponseDTO> generateBooks(
      @RequestBody @Valid MessageRequestDTO messageRequestDTO) {
    return ResponseEntity.ok(this.bookService.generateBooks(messageRequestDTO));
  }

  @PostMapping("/books")
  ResponseEntity<BookResponseDTO> saveBook(
      @Valid @RequestBody BookRequestDTO bookRequestDTO, Principal principal) {

    BookResponseDTO bookResponseDTO = this.bookService.saveBook(bookRequestDTO, principal);

    return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bookResponseDTO.userId())
                .toUri())
        .body(bookResponseDTO);
  }

  @GetMapping("/books")
  ResponseEntity<List<BookTitleResponseDTO>> getBooks() {
    return ResponseEntity.ok(this.bookService.findAllBooks());
  }
}
