package lt.techin.bookreservationapp.user_book;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lt.techin.bookreservationapp.book.MessageRequestDTO;
import lt.techin.bookreservationapp.book.MessageResponseDTO;
import lt.techin.bookreservationapp.rate_limiting.WithRateLimitProtection;
import lt.techin.bookreservationapp.user.User;
import lt.techin.bookreservationapp.user.UserRepository;

@RestController
class UserBookController {

  private final UserBookService userBookService;
  private final UserRepository userRepository;

  UserBookController(
      UserRepository userRepository,
      UserBookService bookService) {
    this.userRepository = userRepository;
    this.userBookService = bookService;
  }

  @PostMapping("/generate-books")
  @WithRateLimitProtection
  ResponseEntity<MessageResponseDTO> generateBooks(
      @RequestBody @Valid MessageRequestDTO messageRequestDTO) {
    return ResponseEntity.ok(this.userBookService.generateBooks(messageRequestDTO));
  }

  @PostMapping("/books")
  ResponseEntity<UserBookResponseDTO> saveUserBook(
      @Valid @RequestBody UserBookRequestDTO userBookRequestDTO, Principal principal) {

    UserBookResponseDTO userBookResponseDTO = this.userBookService
        .saveUserBook(userBookRequestDTO, principal);

    String email = principal.getName();

    User user = this.userRepository.findByEmail(email)
        .orElseThrow();

    return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}/users/{userId}")
        .buildAndExpand(userBookResponseDTO.bookId(), user.getId())
        .toUri())
        .body(userBookResponseDTO);
  }

  // TODO: write a test, do I not see books on my user if saved books for other
  // user?
  @GetMapping("/books")
  ResponseEntity<List<UserBookTitleResponseDTO>> getUserBooks() {
    return ResponseEntity.ok(this.userBookService.findAllUserBooks());
  }
}
