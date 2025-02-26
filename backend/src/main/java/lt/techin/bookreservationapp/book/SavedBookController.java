package lt.techin.bookreservationapp.book;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import lt.techin.bookreservationapp.DTOs.SavedBookDTO;
import lt.techin.bookreservationapp.entities.SavedBook;
import lt.techin.bookreservationapp.entities.User;
import lt.techin.bookreservationapp.repositories.SavedBookRepository;
import lt.techin.bookreservationapp.repositories.UserRepository;

@RestController
class SavedBookController {

  private final SavedBookRepository savedBookRepository;
  private final UserRepository userRepository;

  SavedBookController(SavedBookRepository savedBookRepository, UserRepository userRepository) {
    this.savedBookRepository = savedBookRepository;
    this.userRepository = userRepository;
  }

  //  @PostMapping("/save-book")
  //  public SavedBook saveBook(@RequestBody SavedBookDTO savedBookDTO, Principal principal) {
  //    Optional<User> user = this.userRepository.findByEmail(principal.getName());
  //
  //    if (user.isPresent()) {
  //      SavedBook savedBook = new SavedBook();
  //      savedBook.setTitle(savedBookDTO.title());
  //      savedBook.setUser(user.get());
  //
  //      this.savedBookRepository.save(savedBook);
  //    }
  //
  //    return null;
  //  }

  @PostMapping("/save-book")
  ResponseEntity<SavedBook> saveBook(
      @Valid @RequestBody SavedBookDTO savedBookDTO, Principal principal) {
    User user =
        this.userRepository
            .findByEmail(principal.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    SavedBook savedBook = new SavedBook(savedBookDTO.title(), user);
    this.savedBookRepository.save(savedBook);

    return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
  }

  @GetMapping("/saved-books")
  ResponseEntity<List<String>> getBooks() {
    return ResponseEntity.status(HttpStatus.OK).body(this.savedBookRepository.findAllTitles());
  }
}
