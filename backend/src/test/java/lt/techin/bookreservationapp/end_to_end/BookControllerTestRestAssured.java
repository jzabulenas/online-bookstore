package lt.techin.bookreservationapp.end_to_end;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lt.techin.bookreservationapp.book.Book;
import lt.techin.bookreservationapp.book.BookRepository;
import lt.techin.bookreservationapp.book.MessageRequestDTO;
import lt.techin.bookreservationapp.role.Role;
import lt.techin.bookreservationapp.role.RoleRepository;
import lt.techin.bookreservationapp.user.User;
import lt.techin.bookreservationapp.user.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BookControllerTestRestAssured {

  @LocalServerPort private Integer port;

  static MariaDBContainer<?> mariaDB =
      new MariaDBContainer<>(DockerImageName.parse("mariadb:10.11"));

  @BeforeAll
  static void beforeAll() {
    mariaDB.start();
  }

  @AfterAll
  static void afterAll() {
    mariaDB.stop();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mariaDB::getJdbcUrl);
    registry.add("spring.datasource.username", mariaDB::getUsername);
    registry.add("spring.datasource.password", mariaDB::getPassword);
  }

  @Autowired UserRepository userRepository;
  @Autowired RoleRepository roleRepository;
  @Autowired PasswordEncoder passwordEncoder;
  @Autowired BookRepository bookRepository;

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost:" + port;
    this.bookRepository.deleteAll();
    this.userRepository.deleteAll();
  }

  // generateBooks
  //
  //
  //
  //
  //
  //
  //
  //
  //

  @Test
  void generateBooks_whenBookIsGenerated_return200AndListOfBooks() throws JsonProcessingException {
    Optional<Role> role = this.roleRepository.findByName("ROLE_USER");

    User user =
        this.userRepository.save(
            new User(
                "jurgis@inbox.lt", passwordEncoder.encode("123456"), List.of(role.orElseThrow())));

    Response csrfResponse = given().when().get("/open").then().extract().response();

    String csrfToken = csrfResponse.cookie("XSRF-TOKEN");

    ObjectMapper objectMapper = new ObjectMapper();

    Response response =
        given()
            .cookie("XSRF-TOKEN", csrfToken)
            .header("X-XSRF-TOKEN", csrfToken)
            .contentType(ContentType.URLENC)
            .body("username=jurgis%40inbox.lt&password=123456")
            .post("/login")
            .then()
            .statusCode(200)
            .extract()
            .response();

    given()
        .cookie("JSESSIONID", response.getSessionId())
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(new MessageRequestDTO("Dracula by Bram Stoker")))
        .post("/generate-books")
        .then()
        .statusCode(200)
        .body(".", aMapWithSize(1))
        .body("result", hasSize(3));
  }

  @Test
  void generateBooks_whenMessageIsNull_return400AndMessage() throws JsonProcessingException {
    Optional<Role> role = this.roleRepository.findByName("ROLE_USER");

    User user =
        this.userRepository.save(
            new User(
                "jurgis@inbox.lt", passwordEncoder.encode("123456"), List.of(role.orElseThrow())));

    Response csrfResponse = given().when().get("/open").then().extract().response();

    String csrfToken = csrfResponse.cookie("XSRF-TOKEN");

    ObjectMapper objectMapper = new ObjectMapper();

    Response response =
        given()
            .cookie("XSRF-TOKEN", csrfToken)
            .header("X-XSRF-TOKEN", csrfToken)
            .contentType(ContentType.URLENC)
            .body("username=jurgis%40inbox.lt&password=123456")
            .post("/login")
            .then()
            .statusCode(200)
            .extract()
            .response();

    given()
        .cookie("JSESSIONID", response.getSessionId())
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(new MessageRequestDTO(null)))
        .post("/generate-books")
        .then()
        .statusCode(400)
        .body("message", equalTo("must not be null"))
        .body(".", aMapWithSize(1));
  }

  // TODO: test to check if same books are not generated as previously?

  // getBooks
  //
  //
  //
  //
  //
  //
  //
  //
  //

  @Test
  void getBooks_whenAuthenticated_returnBooksAnd200() {
    Optional<Role> role = this.roleRepository.findByName("ROLE_USER");

    User user =
        this.userRepository.save(
            new User(
                "jurgis@inbox.lt", passwordEncoder.encode("123456"), List.of(role.orElseThrow())));

    Book bookOne = this.bookRepository.save(new Book("Pride and Prejudice by Jane Austen", user));

    Book bookTwo =
        this.bookRepository.save(new Book("Romeo and Juliet by William Shakespeare", user));

    Response csrfResponse = given().when().get("/open").then().extract().response();

    String csrfToken = csrfResponse.cookie("XSRF-TOKEN");

    Response response =
        given()
            .cookie("XSRF-TOKEN", csrfToken)
            .header("X-XSRF-TOKEN", csrfToken)
            .contentType(ContentType.URLENC)
            .body("username=jurgis%40inbox.lt&password=123456")
            .post("/login")
            .then()
            .statusCode(200)
            .extract()
            .response();

    given()
        .cookie("JSESSIONID", response.getSessionId())
        .get("/books")
        .then()
        .statusCode(200)
        .body(".", hasSize(2))
        .body("[0].title", equalTo(bookOne.getTitle()))
        .body("[0]", aMapWithSize(1))
        .body("[1].title", equalTo(bookTwo.getTitle()))
        .body("[1]", aMapWithSize(1));
  }
}
