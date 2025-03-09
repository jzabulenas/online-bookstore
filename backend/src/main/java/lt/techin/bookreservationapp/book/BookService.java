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
            .user(
                "I have read "
                    + messageRequestDTO.message()
                    + " and liked it. Suggest me 3 new books to read. Only provide title, and author. It should adhere this format: Book name by Author. The result should be comma separated. Do not provide any introduction, like \"Here are three...\". "
                    + "Return only the comma separated values. "
                    + "For example, the result should be like this: Lorem Ipsum by Lorem Ipsum,Lorem Ipsum by Lorem Ipsum,Lorem Ipsum by Lorem Ipsum"
                    + "Do not include any backticks in the result. Do not use any let keywords, just the comma separated values. "
                    + "Also make sure to not include these books in the result: "
                    + titles)
            .call()
            .content();

    String[] books = result.split(",");

    return new MessageResponseDTO(books);
  }

  BookResponseDTO saveBook(BookRequestDTO bookRequestDTO, Principal principal) {
    User user =
        this.userRepository
            .findByEmail(principal.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    Book savedBook = this.bookRepository.save(new Book(bookRequestDTO.title(), user));

    return new BookResponseDTO(savedBook.getTitle(), savedBook.getUser().getId());
  }
}
