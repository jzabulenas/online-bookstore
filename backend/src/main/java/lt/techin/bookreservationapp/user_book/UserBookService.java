package lt.techin.bookreservationapp.user_book;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lt.techin.bookreservationapp.book.Book;
import lt.techin.bookreservationapp.book.BookRepository;
import lt.techin.bookreservationapp.book.BookTitleAlreadyExistsException;
import lt.techin.bookreservationapp.book.MessageRequestDTO;
import lt.techin.bookreservationapp.book.MessageResponseDTO;
import lt.techin.bookreservationapp.user.User;
import lt.techin.bookreservationapp.user.UserRepository;

import org.jspecify.annotations.Nullable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@Service
class UserBookService {

  private final ChatClient chatClient;
  private final RestClient restClient;
  private final BookRepository bookRepository;
  private final UserRepository userRepository;
  private final UserBookRepository userBookRepository;
  // I do this in hopes that it does not generate same books on consecutive call
  private @Nullable String resultFromApiCall;
  private final Map<String, Boolean> bookExistsCache = new ConcurrentHashMap<>();

  @Autowired
  UserBookService(
      ChatClient chatClient,
      RestClient.Builder restClientBuilder,
      @Value("${open-library.user-agent}") String userAgent,
      BookRepository bookRepository,
      UserRepository userRepository,
      UserBookRepository userBookRepository) {
    this.chatClient = chatClient;
    this.restClient = restClientBuilder
        .baseUrl("https://openlibrary.org")
        .defaultHeader("User-Agent", userAgent)
        .build();
    this.bookRepository = bookRepository;
    this.userRepository = userRepository;
    this.userBookRepository = userBookRepository;
  }

  MessageResponseDTO generateBooks(MessageRequestDTO messageRequestDTO) {
    List<String> titles = this.userBookRepository.findAllTitles();

    String result = this.chatClient
        .prompt()
        .user(
            "I have read "
                + messageRequestDTO.message()
                + " and liked it. Suggest me 6 books that closely match its specific themes, subject matter, writing style, and tone — not just its broad genre. They must adhere this format: 'Amet Consectetur by Adipisicing Elit|Necessitatibus Eum by Numquam Architecto|Eem Illum by Dolorem Error'."
                + " Return only the six pipe-separated values. Also make sure to not include these books in the result: "
                + titles)
        .call()
        .content();

    String[] verified = Arrays.stream(Objects.requireNonNull(result).split("\\|"))
        .map(String::trim)
        .filter(this::bookExists)
        .limit(3)
        .toArray(String[]::new);
    // TODO:
    // Wait a minute... is "resultFromApiCall" bound to each and every user?
    // And even then, if Playwright for each browser calls this endpoint again,
    // doesn't that create problems?
    // Disabling for now
    // resultFromApiCall = result;
    // System.out.println(resultFromApiCall);

    return new MessageResponseDTO(verified);
  }

  private boolean bookExists(String candidate) {
    return this.bookExistsCache.computeIfAbsent(candidate, this::lookupBook);
  }

  private boolean lookupBook(String candidate) {
    String[] parts = candidate.split(" by ", 2);
    if (parts.length < 2)
      return false;

    String title = parts[0].trim();
    String author = parts[1].trim();

    try {
      OpenLibraryResponse response = this.restClient
          .get()
          .uri("/search.json?title={title}&author={author}&limit=1", title, author)
          .retrieve()
          .body(OpenLibraryResponse.class);
      return response != null && response.numFound() > 0;
    } catch (Exception e) {
      return false;
    }
  }

  private record OpenLibraryResponse(int numFound) {
  }

  UserBookResponseDTO saveUserBook(UserBookRequestDTO userBookRequestDTO, Principal principal) {

    User user = this.userRepository
        .findByEmail(principal.getName())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    if (this.userBookRepository.existsByBookTitleAndUser(userBookRequestDTO.title(), user)) {
      throw new BookTitleAlreadyExistsException();
    }

    if (this.bookRepository.existsByTitle(userBookRequestDTO.title())) {
      Book book = this.bookRepository.findByTitle(userBookRequestDTO.title()).orElseThrow();

      UserBook savedUserBook = this.userBookRepository.save(new UserBook(user, book));

      return UserBookMapper.toDTO(savedUserBook);
    } else {
      Book book = this.bookRepository.save(new Book(userBookRequestDTO.title(), null));

      UserBook savedUserBook = this.userBookRepository.save(new UserBook(user, book));

      return UserBookMapper.toDTO(savedUserBook);
    }
  }

  List<UserBookTitleResponseDTO> findAllUserBooks() {

    List<String> titles = this.userBookRepository.findAllTitles();

    return UserBookMapper.toEntities(titles);
  }
}
