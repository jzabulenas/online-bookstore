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
class BookController {

  private final ChatClient chatClient;
  private final BookRepository bookRepository;
  private final UserRepository userRepository;

  BookController(
      ChatClient chatClient, BookRepository bookRepository, UserRepository userRepository) {
    this.chatClient = chatClient;
    this.bookRepository = bookRepository;
    this.userRepository = userRepository;
  }

  @PostMapping("/generate-books")
  String generateBooks(@RequestBody String message) {
    List<String> titles = this.bookRepository.findAllTitles();

    String result =
        chatClient
            .prompt()
            .user(
                "I have read "
                    + message
                    + " and liked it. Suggest me 3 new books to read. Only provide title, and author. It should adhere this format: \"Book name by Author\". The result should be stored in a JavaScript array. Do not provide any introduction, like \"Here are three...\". "
                    + "Return only the array with values. "
                    + "For example, the result should be like this: [\"Lorem Ipsum by Lorem Ipsum\", \"Lorem Ipsum by Lorem Ipsum\", \"Lorem Ipsum by Lorem Ipsum\"]. "
                    + "Do not include any backticks in the result. Do not use any let keywords, just the array with data. "
                    + "Also make sure to not include these books in the result: "
                    + titles)
            .call()
            .content();

    System.out.println(result);
    System.out.println("---");
    System.out.println();

    return result;
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
