package lt.techin.bookreservationapp.book;

import org.springframework.web.bind.annotation.RestController;

import lt.techin.bookreservationapp.user.UserRepository;

@RestController
class SavedBookController {

  private final BookRepository bookRepository;
  private final UserRepository userRepository;

  SavedBookController(BookRepository bookRepository, UserRepository userRepository) {
    this.bookRepository = bookRepository;
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

  //  @PostMapping("/save-book")
  //  ResponseEntity<Book> saveBook(
  //      @Valid @RequestBody SavedBookDTO savedBookDTO, Principal principal) {
  //    User user =
  //        this.userRepository
  //            .findByEmail(principal.getName())
  //            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not
  // found"));
  //
  //    Book savedBook = new Book(savedBookDTO.title(), user);
  //    this.bookRepository.save(savedBook);
  //
  //    return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
  //  }

  //  @GetMapping("/saved-books")
  //  ResponseEntity<List<String>> getBooks() {
  //    return ResponseEntity.status(HttpStatus.OK).body(this.bookRepository.findAllTitles());
  //  }
}
