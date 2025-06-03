package lt.techin.bookreservationapp.book;

import java.security.Principal;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lt.techin.bookreservationapp.user.User;
import lt.techin.bookreservationapp.user.UserRepository;
import lt.techin.bookreservationapp.user_book.UserBook;
import lt.techin.bookreservationapp.user_book.UserBookRepository;
import lt.techin.bookreservationapp.user_book.UserBookResponseDTO;

@Service
public class BookService {

  private final ChatClient chatClient;
  private final BookRepository bookRepository;
  private final UserRepository userRepository;
  private final UserBookRepository userBookRepository;

  @Autowired
  public BookService(
      ChatClient chatClient, BookRepository bookRepository, UserRepository userRepository,
      UserBookRepository userBookRepository) {
    this.chatClient = chatClient;
    this.bookRepository = bookRepository;
    this.userRepository = userRepository;
    this.userBookRepository = userBookRepository;
  }

  MessageResponseDTO generateBooks(MessageRequestDTO messageRequestDTO) {
    // TODO: validate this? Like, if repository contains a particular book, it
    // should not be in generated result
    List<String> titles = this.userBookRepository.findAllTitles();

    String result = chatClient
        .prompt()
        // .user(
        // "I have read "
        // + messageRequestDTO.message()
        // + " and liked it. Suggest me 3 new books to read. Only provide
        // title, and author. It should adhere this format: 'Lorem, ipsum by Dolor Sit'.
        // The
        // result should be comma separated. Do not provide any introduction, like
        // \"Here are
        // three...\". "
        // + "Return only the three comma separated values. "
        // + "For example, the result should be like this: 'Amet Consectetur
        // by Adipisicing Elit,Necessitatibus Eum by Numquam Architecto,Eem Illum by
        // Dolorem
        // Error'"
        // + "Do not include any backticks in the result. Do not use any let
        // keywords, just the three comma separated books. "
        // + "Also make sure to not include these books in the result: "
        // + titles)
        .user("I have read "
            + messageRequestDTO.message()
            + " and liked it. Suggest me 3 new books to read. They must adhere this format: 'Amet Consectetur by Adipisicing Elit|Necessitatibus Eum by Numquam Architecto|Eem Illum by Dolorem Error'."
            + "Return only the three comma separated values. Also make sure to not include these books in the result: "
            + titles)
        .call()
        .content();

    String[] books = result.split("\\|");

    return new MessageResponseDTO(books);
  }

  UserBookResponseDTO saveBook(BookRequestDTO bookRequestDTO, Principal principal) {

    User user = this.userRepository
        .findByEmail(principal.getName())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    if (this.userBookRepository.existsByBookTitleAndUser(bookRequestDTO.title(), user)) {
      throw new BookTitleAlreadyExistsException();
    }

    if (this.bookRepository.existsByTitle(bookRequestDTO.title())) {
      Book book = this.bookRepository.findByTitle(bookRequestDTO.title()).orElseThrow();

      UserBook savedUserBook = this.userBookRepository.save(new UserBook(user, book));

      return new UserBookResponseDTO(savedUserBook.getId(), savedUserBook.getUser().getId(),
          savedUserBook.getBook().getId());
    } else {
      Book book = this.bookRepository.save(new Book(bookRequestDTO.title(), null));

      UserBook savedUserBook = this.userBookRepository.save(new UserBook(user, book));

      return new UserBookResponseDTO(savedUserBook.getId(), savedUserBook.getUser().getId(),
          savedUserBook.getBook().getId());
    }

  }

  List<BookTitleResponseDTO> findAllBooks() {

    List<String> titles = this.userBookRepository.findAllTitles();

    List<BookTitleResponseDTO> books = titles.stream()
        .map(title -> new BookTitleResponseDTO(title))
        .toList();

    return books;
  }
}
