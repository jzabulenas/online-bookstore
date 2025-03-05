package lt.techin.bookreservationapp.book;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  String generateBooks(String message) {
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

    return result;
  }
}
