package lt.techin.bookreservationapp.controllers;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lt.techin.bookreservationapp.repositories.SavedBookRepository;

@RestController
public class BookController {

  private final ChatClient chatClient;
  private final SavedBookRepository savedBookRepository;

  public BookController(ChatClient chatClient, SavedBookRepository savedBookRepository) {
    this.chatClient = chatClient;
    this.savedBookRepository = savedBookRepository;
  }

  @PostMapping("/generate-books")
  public String generateBooks(@RequestBody String message) {
    List<String> titles = this.savedBookRepository.findAllTitles();

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
}
