package lt.techin.bookreservationapp;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

// TODO: should log in in frontend fields have any validation. Also, the login frontend fail test
// pass is 12345
// TODO: sign up frontend tests, for example, password is too short is 12345. Obviously, have to add
// same validation as in backend. Confirm password test is also sussy
// TODO: should frontend email validation on signup be similar to backend? Maybe use server
// validation
// TODO: update 400 validations in frontend, so it always check the message too coming from server
// TODO: Place backend API under HTTPS?
// TODO: Write test if user is unverified and such
@SpringBootApplication
class BookReservationAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(BookReservationAppApplication.class, args);
  }

  @Bean
  ChatClient chatClient(ChatClient.Builder builder) {
    return builder
      .defaultSystem(
        "You are a book recommendation assistant. Only recommend books that genuinely exist and have been published. Never invent or fabricate book titles or authors."
      )
      .build();
  }
}
