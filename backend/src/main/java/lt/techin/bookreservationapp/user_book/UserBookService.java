package lt.techin.bookreservationapp.user_book;

import java.security.Principal;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lt.techin.bookreservationapp.book.Book;
import lt.techin.bookreservationapp.book.BookRepository;
import lt.techin.bookreservationapp.book.BookTitleAlreadyExistsException;
import lt.techin.bookreservationapp.book.MessageRequestDTO;
import lt.techin.bookreservationapp.book.MessageResponseDTO;
import lt.techin.bookreservationapp.user.User;
import lt.techin.bookreservationapp.user.UserRepository;

@Service
class UserBookService {

  private final ChatClient chatClient;
  private final BookRepository bookRepository;
  private final UserRepository userRepository;
  private final UserBookRepository userBookRepository;
  // I do this in hopes that it does not generate same books on consecutive call
  private String resultFromApiCall;

  @Autowired
  UserBookService(
      ChatClient chatClient, BookRepository bookRepository, UserRepository userRepository,
      UserBookRepository userBookRepository) {
    this.chatClient = chatClient;
    this.bookRepository = bookRepository;
    this.userRepository = userRepository;
    this.userBookRepository = userBookRepository;
  }

  MessageResponseDTO generateBooks(MessageRequestDTO messageRequestDTO) {
    List<String> titles = this.userBookRepository.findAllTitles();

    String result = chatClient
        .prompt()
        .user("I have read "
            + messageRequestDTO.message()
            + " and liked it. Suggest me 3 new books to read. They must adhere this format: 'Amet Consectetur by Adipisicing Elit|Necessitatibus Eum by Numquam Architecto|Eem Illum by Dolorem Error'."
            + "Return only the three comma separated values. Also make sure to not include these books in the result: "
            + titles + " and: " + resultFromApiCall)
        .call()
        .content();

    String[] books = result.split("\\|");
    // TODO:
    // Wait a minute... is "resultFromApiCall" bound to each and every user?
    // And even then, if Playwright for each browser calls this endpoint again,
    // doesn't that create problems?
    // Disabling for now
    // resultFromApiCall = result;
    // System.out.println(resultFromApiCall);

    return new MessageResponseDTO(books);
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

    List<UserBookTitleResponseDTO> books = UserBookMapper.toEntities(titles);

    return books;
  }
}
