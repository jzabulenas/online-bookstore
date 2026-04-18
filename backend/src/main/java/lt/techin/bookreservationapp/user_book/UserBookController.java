package lt.techin.bookreservationapp.user_book;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import lt.techin.bookreservationapp.book.MessageRequestDTO;
import lt.techin.bookreservationapp.book.MessageResponseDTO;
import lt.techin.bookreservationapp.rate_limiting.GenerateBooksRateLimitService;
import lt.techin.bookreservationapp.user.User;
import lt.techin.bookreservationapp.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
class UserBookController {

  private final UserBookService userBookService;
  private final UserRepository userRepository;
  private final GenerateBooksRateLimitService generateBooksRateLimitService;

  UserBookController(
    UserRepository userRepository,
    UserBookService bookService,
    GenerateBooksRateLimitService generateBooksRateLimitService
  ) {
    this.userRepository = userRepository;
    this.userBookService = bookService;
    this.generateBooksRateLimitService = generateBooksRateLimitService;
  }

  @PostMapping("/generate-books")
  ResponseEntity<MessageResponseDTO> generateBooks(
    @RequestBody @Valid MessageRequestDTO messageRequestDTO,
    Authentication authentication
  ) {
    User user = (User) authentication.getPrincipal();

    this.generateBooksRateLimitService.checkLimit(
      Objects.requireNonNull(user).getId(),
      "/generate-books"
    );

    return ResponseEntity.ok(
      this.userBookService.generateBooks(messageRequestDTO)
    );
  }

  @PostMapping("/books")
  ResponseEntity<UserBookResponseDTO> saveUserBook(
    @Valid @RequestBody UserBookRequestDTO userBookRequestDTO,
    Principal principal
  ) {
    UserBookResponseDTO userBookResponseDTO = this.userBookService.saveUserBook(
      userBookRequestDTO,
      principal
    );

    String email = principal.getName();

    User user = this.userRepository.findByEmail(email).orElseThrow();

    return ResponseEntity.created(
      ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}/users/{userId}")
        .buildAndExpand(userBookResponseDTO.bookId(), user.getId())
        .toUri()
    ).body(userBookResponseDTO);
  }

  @GetMapping("/books")
  // TODO: should I add that rate limit annotation here, and remove from
  // generateBooks?
  ResponseEntity<List<UserBookTitleResponseDTO>> getUserBooks() {
    return ResponseEntity.ok(this.userBookService.findAllUserBooks());
  }
}
