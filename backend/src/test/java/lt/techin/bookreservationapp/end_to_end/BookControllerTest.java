package lt.techin.bookreservationapp.end_to_end;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import lt.techin.bookreservationapp.book.Book;
import lt.techin.bookreservationapp.book.BookRepository;
import lt.techin.bookreservationapp.book.MessageRequestDTO;
import lt.techin.bookreservationapp.role.Role;
import lt.techin.bookreservationapp.role.RoleRepository;
import lt.techin.bookreservationapp.security.SecurityConfig;
import lt.techin.bookreservationapp.user.User;
import lt.techin.bookreservationapp.user.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BookControllerTest {

  @Autowired BookRepository bookRepository;
  @Autowired UserRepository userRepository;
  @Autowired RoleRepository roleRepository;

  @LocalServerPort private Integer port;

  @Autowired private MockMvc mockMvc;

  static MariaDBContainer<?> mariaDBContainer =
      new MariaDBContainer<>(DockerImageName.parse("mariadb:10.11"));

  @BeforeAll
  static void beforeAll() {
    // TODO: Why can't I call this with this?
    mariaDBContainer.start();
  }

  @AfterAll
  static void afterAll() {
    mariaDBContainer.stop();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mariaDBContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mariaDBContainer::getUsername);
    registry.add("spring.datasource.password", mariaDBContainer::getPassword);
  }

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost:" + port;
    this.bookRepository.deleteAll();
  }

  //
  // generateBooks
  //

  // TODO: throws vs. try catch block here
  @Test
  @WithMockUser(username = "jurgis@gmail.com")
  void generateBooks_whenBookIsGenerated_return200AndListOfBooks() throws Exception {

    ObjectMapper objectMapper = new ObjectMapper();

    this.mockMvc
        .perform(
            post("/generate-books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new MessageRequestDTO("Gabagol")))
                .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("length()").value(3));
  }

  @Test
  void generateBooks_whenUnauthenticated_thenReturn302() throws Exception {

    this.mockMvc
        .perform(
            post("/generate-books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("Gabagol")
                .with(csrf()))
        .andExpect(status().isFound());
  }

  //
  // getBooks
  //

  @Test
  @WithMockUser(username = "jurgis@gmail.com")
  void getBooks_whenCalled_returnBooksAnd200() throws Exception {

    Optional<Role> role = this.roleRepository.findByName("ROLE_USER");

    User user = this.userRepository.save(new User("jurgis@gmail.com", List.of(role.orElseThrow())));

    this.bookRepository.save(new Book("Edward III: The Perfect King", user));
    this.bookRepository.save(
        new Book(
            "The Greatest Traitor: The Life of Sir Roger Mortimer, Ruler of England 1327–1330",
            user));

    this.mockMvc
        .perform(get("/books"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("length()").value(2));

    //    given()
    //        .contentType(ContentType.JSON)
    //        .when()
    //        .get("/api/customers")
    //        .then()
    //        .statusCode(200)
    //        .body(".", hasSize(2));
  }
}
