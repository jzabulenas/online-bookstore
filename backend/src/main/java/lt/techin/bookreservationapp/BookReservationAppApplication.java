package lt.techin.bookreservationapp;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

// TODO: should log in in frontend fields have any validation. Also, the login frotnend fail test
// pass is 12345
// TODO: sign up frotend tests, for example, password is too short is 12345. Obviously, have to add
// same validation as in backend. Confirm password test is also sussy
// TODO: should frontend email valdation on signup be similar to backend? Maybe use server
// valdiation
// TODO: update 400 valdiations in frontend, so it always check the message too coming from server
// TODO: Place backend API under HTTPS?
// TODO: Write test if user is unverified and such
@SpringBootApplication
class BookReservationAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(BookReservationAppApplication.class, args);
  }

  @Bean
  ChatClient chatClient(ChatClient.Builder builder) {
    return builder.build();
  }
}
