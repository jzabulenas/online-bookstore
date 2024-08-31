package lt.techin.bookreservationapp.controllers;

import java.util.Map;

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
  public Map<String, String> generateBooks(@RequestBody String message) {
    return Map.of("result", chatClient.prompt().user(message).call().content());
  }
}
