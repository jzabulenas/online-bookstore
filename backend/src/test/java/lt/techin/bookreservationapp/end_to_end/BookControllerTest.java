package lt.techin.bookreservationapp.end_to_end;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
import io.restassured.specification.RequestSpecification;
import lt.techin.bookreservationapp.book.Book;
import lt.techin.bookreservationapp.book.BookRepository;
import lt.techin.bookreservationapp.book.BookRequestDTO;
import lt.techin.bookreservationapp.book.MessageRequestDTO;
import lt.techin.bookreservationapp.role.Role;
import lt.techin.bookreservationapp.role.RoleRepository;
import lt.techin.bookreservationapp.user.User;
import lt.techin.bookreservationapp.user.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BookControllerTest {

  @LocalServerPort
  private Integer port;

  static MariaDBContainer<?> mariaDB = new MariaDBContainer<>(
      DockerImageName.parse("mariadb:10.11"));

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

  @Autowired
  UserRepository userRepository;
  @Autowired
  RoleRepository roleRepository;
  @Autowired
  PasswordEncoder passwordEncoder;
  @Autowired
  BookRepository bookRepository;

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
  void generateBooks_whenBookIsGenerated_thenReturn200AndListOfBooks()
      throws JsonProcessingException {
    createUser();
    String csrfToken = getCsrfToken();
    Response loginResponse = loginAndGetSession(csrfToken);

    given()
        .cookie("JSESSIONID", loginResponse.getSessionId())
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(new ObjectMapper()
            .writeValueAsString(new MessageRequestDTO("Dracula by Bram Stoker")))
        .when()
        .post("/generate-books")
        .then()
        .statusCode(200)
        .body("$", aMapWithSize(1))
        .body("result", hasSize(3));
  }

  @Test
  void generateBooks_whenMessageIsNull_thenReturn400AndMessage() throws JsonProcessingException {
    createUser();
    String csrfToken = getCsrfToken();
    Response loginResponse = loginAndGetSession(csrfToken);

    given()
        .cookie("JSESSIONID", loginResponse.getSessionId())
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(new ObjectMapper().writeValueAsString(new MessageRequestDTO(null)))
        .when()
        .post("/generate-books")
        .then()
        .statusCode(400)
        .body("message", equalTo("must not be null"))
        .body("$", aMapWithSize(1));
  }

  @Test
  void generateBooks_whenMessageIsTooShort_thenReturn400AndMessage()
      throws JsonProcessingException {
    createUser();
    String csrfToken = getCsrfToken();
    Response loginResponse = loginAndGetSession(csrfToken);

    given()
        .cookie("JSESSIONID", loginResponse.getSessionId())
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(new ObjectMapper().writeValueAsString(new MessageRequestDTO("Fe")))
        .when()
        .post("/generate-books")
        .then()
        .statusCode(400)
        .body("message", equalTo("size must be between 5 and 100"))
        .body("$", aMapWithSize(1));
  }

  @Test
  void generateBooks_whenMessageIsTooLong_thenReturn400AndMessage() throws JsonProcessingException {
    createUser();
    String csrfToken = getCsrfToken();
    Response loginResponse = loginAndGetSession(csrfToken);

    given()
        .cookie("JSESSIONID", loginResponse.getSessionId())
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(new ObjectMapper()
            .writeValueAsString(new MessageRequestDTO(
                "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean mj")))
        .when()
        .post("/generate-books")
        .then()
        .statusCode(400)
        .body("message", equalTo("size must be between 5 and 100"))
        .body("$", aMapWithSize(1));
  }

  @Test
  void generateBooks_whenCalledTwiceWithSameInput_thenResultsShouldDiffer()
      throws JsonProcessingException {
    createUser();
    String csrfToken = getCsrfToken();
    Response loginResponse = loginAndGetSession(csrfToken);

    // Prepare request setup
    RequestSpecification spec = given()
        .cookie("JSESSIONID", loginResponse.getSessionId())
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(new ObjectMapper()
            .writeValueAsString(new MessageRequestDTO("Dracula by Bram Stoker")));

    // First response
    Response first = spec.when()
        .post("/generate-books")
        .then()
        .statusCode(200)
        .body("$", aMapWithSize(1))
        .body("result", hasSize(3))
        .extract()
        .response();
    List<String> firstResult = first.jsonPath().getList("result");

    // Second response
    Response second = spec.when()
        .post("/generate-books")
        .then()
        .statusCode(200)
        .body("$", aMapWithSize(1))
        .body("result", hasSize(3))
        .extract()
        .response();
    List<String> secondResult = second.jsonPath().getList("result");

    // Assertion that results are different
    assertNotEquals(firstResult, secondResult, "Expected different results for repeated calls");
  }

  @Test
  void generateBooks_whenUnauthenticated_thenReturn401() throws JsonProcessingException {
    given()
        .contentType(ContentType.JSON)
        .body(new ObjectMapper()
            .writeValueAsString(new MessageRequestDTO("Dracula by Bram Stoker")))
        .when()
        .post("/generate-books")
        .then()
        .statusCode(401)
        .body(emptyOrNullString());
  }

  @Test
  void generateBooks_whenAuthenticatedButNoCSRF_thenReturn403AndBody()
      throws JsonProcessingException {
    createUser();
    String csrfToken = getCsrfToken();
    Response loginResponse = loginAndGetSession(csrfToken);

    given()
        .cookie("JSESSIONID", loginResponse.getSessionId())
        .contentType(ContentType.JSON)
        .body(new ObjectMapper()
            .writeValueAsString(new MessageRequestDTO("Dracula by Bram Stoker")))
        .when()
        .post("/generate-books")
        .then()
        .statusCode(403)
        .body("timestamp", containsString("2025"))
        .body("status", equalTo(403))
        .body("error", equalTo("Forbidden"))
        .body("path", equalTo("/generate-books"));
  }

  // TODO: test to check if same books are not generated as previously?

  // saveBook
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
  void saveBook_whenBookIsSaved_thenReturn201AndBody() throws JsonProcessingException {
    User user = createUser();
    String csrfToken = getCsrfToken();
    Response loginResponse = loginAndGetSession(csrfToken);

    given()
        .cookie("JSESSIONID", loginResponse.getSessionId())
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(new ObjectMapper().writeValueAsString(new BookRequestDTO("Dracula by Bram Stoker")))
        .when()
        .post("/books")
        .then()
        .statusCode(201)
        .body("title", equalTo("Dracula by Bram Stoker"))
        .body("userId", equalTo(user.getId().intValue()))
        .body("$", aMapWithSize(2))
        .header("Location", containsString("/books/" + user.getId()));
  }

  @Test
  void saveBook_whenTitleAlreadyExistsForUser_thenReturn400AndMessage()
      throws JsonProcessingException {
    User user = createUser();
    String csrfToken = getCsrfToken();
    Response loginResponse = loginAndGetSession(csrfToken);
    this.bookRepository.save(new Book("Dracula by Bram Stoker", user));

    given()
        .cookie("JSESSIONID", loginResponse.getSessionId())
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(new ObjectMapper().writeValueAsString(new BookRequestDTO("Dracula by Bram Stoker")))
        .when()
        .post("/books")
        .then()
        .statusCode(400)
        .body("title", equalTo("Already exists"))
        .body("$", aMapWithSize(1));
  }

  @Test
  void saveBook_whenTitleAlreadyExistsForOtherUser_thenReturn201AndMessage()
      throws JsonProcessingException {
    User user = createUser();
    String csrfToken = getCsrfToken();
    Response loginResponse = loginAndGetSession(csrfToken);

    Optional<Role> role = this.roleRepository.findByName("ROLE_USER");
    User otherUser = this.userRepository
        .save(new User("antanas@inbox.lt", passwordEncoder.encode("123456"),
            List.of(role.orElseThrow())));
    this.bookRepository.save(new Book("Dracula by Bram Stoker", otherUser));

    given()
        .cookie("JSESSIONID", loginResponse.getSessionId())
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(new ObjectMapper().writeValueAsString(new BookRequestDTO("Dracula by Bram Stoker")))
        .when()
        .post("/books")
        .then()
        .statusCode(201)
        .body("title", equalTo("Dracula by Bram Stoker"))
        .body("userId", equalTo(user.getId().intValue()))
        .body("$", aMapWithSize(2))
        .header("Location", containsString("/books/" + user.getId()));
  }

  @Test
  void saveBook_whenUnauthenticatedCalls_thenReturn401() throws JsonProcessingException {
    given()
        .contentType(ContentType.JSON)
        .body(new ObjectMapper().writeValueAsString(new BookRequestDTO("Dracula by Bram Stoker")))
        .when()
        .post("/books")
        .then()
        .statusCode(401)
        .body(emptyOrNullString());
  }

  @Test
  void saveBook_whenAuthenticatedButNoCSRF_thenReturn403AndBody() throws JsonProcessingException {
    createUser();
    String csrfToken = getCsrfToken();
    Response loginResponse = loginAndGetSession(csrfToken);

    given()
        .cookie("JSESSIONID", loginResponse.getSessionId())
        .contentType(ContentType.JSON)
        .body(new ObjectMapper()
            .writeValueAsString(new BookRequestDTO("Dracula by Bram Stoker")))
        .when()
        .post("/books")
        .then()
        .statusCode(403)
        .body("timestamp", containsString("2025"))
        .body("status", equalTo(403))
        .body("error", equalTo("Forbidden"))
        .body("path", equalTo("/books"));
  }

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
  void getBooks_whenCalled_thenReturnBooksAnd200() {
    User user = createUser();
    String csrfToken = getCsrfToken();
    Response loginResponse = loginAndGetSession(csrfToken);

    Book bookOne = this.bookRepository.save(new Book("Pride and Prejudice by Jane Austen", user));

    Book bookTwo = this.bookRepository
        .save(new Book("Romeo and Juliet by William Shakespeare", user));

    given()
        .cookie("JSESSIONID", loginResponse.getSessionId())
        .when()
        .get("/books")
        .then()
        .statusCode(200)
        .body("$", hasSize(2))
        .body("[0].title", equalTo(bookOne.getTitle()))
        .body("[0]", aMapWithSize(1))
        .body("[1].title", equalTo(bookTwo.getTitle()))
        .body("[1]", aMapWithSize(1));
  }

  @Test
  void getBooks_whenListEmpty_thenReturnEmptyListAnd200() {
    createUser();
    String csrfToken = getCsrfToken();
    Response loginResponse = loginAndGetSession(csrfToken);

    given()
        .cookie("JSESSIONID", loginResponse.getSessionId())
        .when()
        .get("/books")
        .then()
        .statusCode(200)
        .body("$", empty());
  }

  @Test
  void getBooks_whenUnauthenticated_thenReturn401AndNoBody() {
    given()
        .when()
        .get("/books")
        .then()
        .statusCode(401)
        .body(emptyOrNullString());
  }

  // Helper methods
  //
  //
  //
  //
  //
  //
  //
  //
  //

  private User createUser() {
    Optional<Role> role = this.roleRepository.findByName("ROLE_USER");

    return this.userRepository.save(new User("jurgis@inbox.lt", passwordEncoder.encode("123456"),
        List.of(role.orElseThrow())));
  }

  private String getCsrfToken() {
    Response csrfResponse = given().when().get("/open").then().extract().response();

    return csrfResponse.cookie("XSRF-TOKEN");
  }

  private Response loginAndGetSession(String csrfToken) {
    return given()
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.URLENC)
        .body("username=jurgis%40inbox.lt&password=123456")
        .post("/login")
        .then()
        .statusCode(200)
        .extract()
        .response();
  }
}
