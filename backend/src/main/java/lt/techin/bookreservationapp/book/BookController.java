package lt.techin.bookreservationapp.book;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lt.techin.bookreservationapp.rate_limiting.WithRateLimitProtection;
import lt.techin.bookreservationapp.user.User;
import lt.techin.bookreservationapp.user.UserRepository;
import lt.techin.bookreservationapp.user_book.UserBookResponseDTO;

@RestController
public class BookController {

  private final BookService bookService;
  private final UserRepository userRepository;

  BookController(
      UserRepository userRepository,
      BookService bookService) {
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
  ResponseEntity<UserBookResponseDTO> saveBook(
      @Valid @RequestBody BookRequestDTO bookRequestDTO, Principal principal) {

    UserBookResponseDTO userBookResponseDTO = this.bookService.saveBook(bookRequestDTO, principal);

    String email = principal.getName();

    User user = this.userRepository.findByEmail(email)
        .orElseThrow();

    return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}/users/{userId}")
        .buildAndExpand(userBookResponseDTO.bookId(), user.getId())
        .toUri())
        .body(userBookResponseDTO);
  }

  @GetMapping("/books")
  ResponseEntity<List<BookTitleResponseDTO>> getBooks() {
    return ResponseEntity.ok(this.bookService.findAllBooks());
  }
}
