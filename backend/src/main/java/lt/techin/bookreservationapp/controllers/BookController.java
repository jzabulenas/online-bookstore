package lt.techin.bookreservationapp.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

  private final ChatClient chatClient;

  public BookController(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @PostMapping("/generate-books")
  public String generateBooks(@RequestBody String message) {
    return chatClient
        .prompt()
        .user(
            "I have read "
                + message
                + " and liked it. Suggest me 3 new books to read. Only provide title, and author. It should adhere this format: \"Book name by Author\". The result should be stored in a JavaScript array. Do not provide any introduction, like \"Here are three...\"")
        .call()
        .content();
  }
}
