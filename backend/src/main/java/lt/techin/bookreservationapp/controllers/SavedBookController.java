package lt.techin.bookreservationapp.controllers;

import java.security.Principal;
import java.util.Optional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lt.techin.bookreservationapp.DTOs.SavedBookDTO;
import lt.techin.bookreservationapp.entities.SavedBook;
import lt.techin.bookreservationapp.entities.User;
import lt.techin.bookreservationapp.repositories.SavedBookRepository;
import lt.techin.bookreservationapp.repositories.UserRepository;

@RestController
public class SavedBookController {

  private final SavedBookRepository savedBookRepository;
  private final UserRepository userRepository;

  public SavedBookController(
      SavedBookRepository savedBookRepository, UserRepository userRepository) {
    this.savedBookRepository = savedBookRepository;
    this.userRepository = userRepository;
  }

  @PostMapping("/save-book")
  public SavedBook saveBook(@RequestBody SavedBookDTO savedBookDTO, Principal principal) {
    Optional<User> user = this.userRepository.findByEmail(principal.getName());

    if (user.isPresent()) {
      SavedBook savedBook = new SavedBook();
      savedBook.setTitle(savedBookDTO.title());
      savedBook.setUser(user.get());

      this.savedBookRepository.save(savedBook);
    }

    return null;
  }
}
