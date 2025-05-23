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

@Service
public class BookService {

  private final ChatClient chatClient;
  private final BookRepository bookRepository;
  private final UserRepository userRepository;

  @Autowired
  public BookService(
      ChatClient chatClient, BookRepository bookRepository, UserRepository userRepository) {
    this.chatClient = chatClient;
    this.bookRepository = bookRepository;
    this.userRepository = userRepository;
  }

  MessageResponseDTO generateBooks(MessageRequestDTO messageRequestDTO) {
    List<String> titles = this.bookRepository.findAllTitles();

    String result =
        chatClient
            .prompt()
            //            .user(
            //                "I have read "
            //                    + messageRequestDTO.message()
            //                    + " and liked it. Suggest me 3 new books to read. Only provide
            // title, and author. It should adhere this format: 'Lorem, ipsum by Dolor Sit'. The
            // result should be comma separated. Do not provide any introduction, like \"Here are
            // three...\". "
            //                    + "Return only the three comma separated values. "
            //                    + "For example, the result should be like this: 'Amet Consectetur
            // by Adipisicing Elit,Necessitatibus Eum by Numquam Architecto,Eem Illum by Dolorem
            // Error'"
            //                    + "Do not include any backticks in the result. Do not use any let
            // keywords, just the three comma separated books. "
            //                    + "Also make sure to not include these books in the result: "
            //                    + titles)
            .user(
                "I have read "
                    + messageRequestDTO.message()
                    + " and liked it. Suggest me 3 new books to read. They must adhere this format: 'Amet Consectetur by Adipisicing Elit|Necessitatibus Eum by Numquam Architecto|Eem Illum by Dolorem Error'."
                    + "Return only the three comma separated values. Also make sure to not include these books in the result: "
                    + titles)
            .call()
            .content();

    String[] books = result.split("\\|");

    return new MessageResponseDTO(books);
  }

  BookResponseDTO saveBook(BookRequestDTO bookRequestDTO, Principal principal) {

    // TODO: validate this?
    User user =
        this.userRepository
            .findByEmail(principal.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    if (this.bookRepository.existsByTitleAndUser(bookRequestDTO.title(), user)) {
      throw new BookTitleAlreadyExistsException();
    }

    Book savedBook = this.bookRepository.save(new Book(bookRequestDTO.title(), user));

    return new BookResponseDTO(savedBook.getTitle(), savedBook.getUser().getId());
  }

  List<BookTitleResponseDTO> findAllBooks() {

    List<String> titles = this.bookRepository.findAllTitles();

    List<BookTitleResponseDTO> books =
        titles.stream().map(title -> new BookTitleResponseDTO(title)).toList();

    return books;
  }
}
