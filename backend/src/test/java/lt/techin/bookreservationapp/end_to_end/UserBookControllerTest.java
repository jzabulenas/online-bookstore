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
import org.junit.jupiter.api.Nested;
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
import lt.techin.bookreservationapp.book.MessageRequestDTO;
import lt.techin.bookreservationapp.role.Role;
import lt.techin.bookreservationapp.role.RoleRepository;
import lt.techin.bookreservationapp.user.User;
import lt.techin.bookreservationapp.user.UserRepository;
import lt.techin.bookreservationapp.user_book.UserBook;
import lt.techin.bookreservationapp.user_book.UserBookRepository;
import lt.techin.bookreservationapp.user_book.UserBookRequestDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserBookControllerTest {

  @LocalServerPort
  private Integer port;

  static MariaDBContainer<?> mariaDB = new MariaDBContainer<>(
      DockerImageName.parse("mariadb:11.4"));

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
  private UserRepository userRepository;
  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private BookRepository bookRepository;
  @Autowired
  private UserBookRepository userBookRepository;

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost:" + port;
    this.userBookRepository.deleteAll();
    this.bookRepository.deleteAll();
    this.userRepository.deleteAll();
  }

  @Nested
  class GenerateBooksTests {

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
    void generateBooks_whenUserBooksExist_thenNotSeeThemInNewlyGeneratedBooks()
        throws JsonProcessingException {
      User user = createUser();
      String csrfToken = getCsrfToken();
      Response loginResponse = loginAndGetSession(csrfToken);

      RequestSpecification spec = given()
          .cookie("JSESSIONID", loginResponse.getSessionId())
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(new ObjectMapper()
              .writeValueAsString(new MessageRequestDTO("Dracula by Bram Stoker")));

      Response responseOne = spec.when()
          .post("/generate-books")
          .then()
          .statusCode(200)
          .body("$", aMapWithSize(1))
          .body("result", hasSize(3))
          .extract()
          .response();
      List<String> resultOne = responseOne.jsonPath().getList("result");

      Book bookOne = bookRepository.save(new Book(resultOne.get(0), null));
      Book bookTwo = bookRepository.save(new Book(resultOne.get(1), null));
      Book bookThree = bookRepository.save(new Book(resultOne.get(2), null));

      userBookRepository.save(new UserBook(user, bookOne));
      userBookRepository.save(new UserBook(user, bookTwo));
      userBookRepository.save(new UserBook(user, bookThree));

      List<String> existingUserBooks = userBookRepository
          .findAllTitlesByEmail(user.getEmail());

      Response response = spec.when()
          .post("/generate-books")
          .then()
          .statusCode(200)
          .body("$", aMapWithSize(1))
          .body("result", hasSize(3))
          .extract()
          .response();
      List<String> result = response.jsonPath().getList("result");

      assertNotEquals(existingUserBooks, result, "Expected different results for repeated calls");
    }

    // Unhappy path
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
    void generateBooks_whenMessageIsTooLong_thenReturn400AndMessage()
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
  }

  @Nested
  class SaveUserBookTests {

    @Test
    void saveUserBook_whenBookIsSaved_thenReturn201AndBody() throws JsonProcessingException {
      User user = createUser();
      String csrfToken = getCsrfToken();
      Response loginResponse = loginAndGetSession(csrfToken);
      String bookTitle = "Dracula by Bram Stoker";

      given()
          .cookie("JSESSIONID", loginResponse.getSessionId())
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(new ObjectMapper().writeValueAsString(new UserBookRequestDTO(bookTitle)))
          .when()
          .post("/books")
          .then()
          .statusCode(201)
          .body("id", equalTo(findUserBookIdByUserIdAndBookTitle(user.getId(), bookTitle)))
          .body("userId", equalTo(user.getId().intValue()))
          .body("bookId", equalTo(findBookIdByTitle(bookTitle)))
          .body("$", aMapWithSize(3))
          .header("Location", containsString("/books/" + findBookIdByTitle(bookTitle) + "/users/"
              + user.getId()));

    }

    // Unhappy path
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
    void saveUserBook_whenTitleAlreadyExistsForUser_thenReturn400AndMessage()
        throws JsonProcessingException {
      User user = createUser();
      String csrfToken = getCsrfToken();
      Response loginResponse = loginAndGetSession(csrfToken);
      Book book = bookRepository.save(new Book("Dracula by Bram Stoker", null));
      userBookRepository.save(new UserBook(user, book));

      given()
          .cookie("JSESSIONID", loginResponse.getSessionId())
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(new ObjectMapper()
              .writeValueAsString(new UserBookRequestDTO("Dracula by Bram Stoker")))
          .when()
          .post("/books")
          .then()
          .statusCode(400)
          .body("title", equalTo("Already exists"))
          .body("$", aMapWithSize(1));
    }

    @Test
    void saveUserBook_whenTitleAlreadyExistsForOtherUser_thenReturn201AndMessage()
        throws JsonProcessingException {
      User user = createUser();
      String csrfToken = getCsrfToken();
      Response loginResponse = loginAndGetSession(csrfToken);
      String bookTitle = "Dracula by Bram Stoker";

      Optional<Role> role = roleRepository.findByName("ROLE_USER");
      User otherUser = userRepository
          .save(new User("antanas@inbox.lt", passwordEncoder.encode("123456"),
              List.of(role.orElseThrow()), null));
      Book book = bookRepository.save(new Book(bookTitle, null));
      userBookRepository.save(new UserBook(otherUser, book));

      given()
          .cookie("JSESSIONID", loginResponse.getSessionId())
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(new ObjectMapper().writeValueAsString(new UserBookRequestDTO(bookTitle)))
          .when()
          .post("/books")
          .then()
          .statusCode(201)
          .body("id", equalTo(findUserBookIdByUserIdAndBookTitle(user.getId(), bookTitle)))
          .body("userId", equalTo(user.getId().intValue()))
          .body("bookId", equalTo(findBookIdByTitle(bookTitle)))
          .body("$", aMapWithSize(3))
          .header("Location", containsString("/books/" + findBookIdByTitle(bookTitle) + "/users/"
              + user.getId()));
    }

    @Test
    void saveUserBook_whenUnauthenticatedCalls_thenReturn401() throws JsonProcessingException {
      given()
          .contentType(ContentType.JSON)
          .body(new ObjectMapper()
              .writeValueAsString(new UserBookRequestDTO("Dracula by Bram Stoker")))
          .when()
          .post("/books")
          .then()
          .statusCode(401)
          .body(emptyOrNullString());
    }

    @Test
    void saveUserBook_whenAuthenticatedButNoCSRF_thenReturn403AndBody()
        throws JsonProcessingException {
      createUser();
      String csrfToken = getCsrfToken();
      Response loginResponse = loginAndGetSession(csrfToken);

      given()
          .cookie("JSESSIONID", loginResponse.getSessionId())
          .contentType(ContentType.JSON)
          .body(new ObjectMapper()
              .writeValueAsString(new UserBookRequestDTO("Dracula by Bram Stoker")))
          .when()
          .post("/books")
          .then()
          .statusCode(403)
          .body("timestamp", containsString("2025"))
          .body("status", equalTo(403))
          .body("error", equalTo("Forbidden"))
          .body("path", equalTo("/books"));
    }
  }

  @Nested
  class GetUserBooksTests {

    @Test
    void getUserBooks_whenCalled_thenReturnBooksAnd200() {
      User user = createUser();
      String csrfToken = getCsrfToken();
      Response loginResponse = loginAndGetSession(csrfToken);

      Book bookOne = bookRepository.save(new Book("Pride and Prejudice by Jane Austen", null));

      Book bookTwo = bookRepository
          .save(new Book("Romeo and Juliet by William Shakespeare", null));

      userBookRepository.save(new UserBook(user, bookOne));
      userBookRepository.save(new UserBook(user, bookTwo));

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
    void getUserBooks_whenOneUserHasBooks_thenOtherUserHasNoneAnd200() {
      User user = createUser();
      Book bookOne = bookRepository.save(new Book("Pride and Prejudice by Jane Austen", null));
      Book bookTwo = bookRepository
          .save(new Book("Romeo and Juliet by William Shakespeare", null));
      userBookRepository.save(new UserBook(user, bookOne));
      userBookRepository.save(new UserBook(user, bookTwo));

      Optional<Role> role = roleRepository.findByName("ROLE_USER");
      userRepository.save(new User("antanas@inbox.lt",
          passwordEncoder.encode("123456"), List.of(role.orElseThrow()), null));
      String csrfToken = getCsrfToken();
      Response response = given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.URLENC)
          .body("username=antanas%40inbox.lt&password=123456")
          .post("/login")
          .then()
          .statusCode(200)
          .extract()
          .response();

      given()
          .cookie("JSESSIONID", response.getSessionId())
          .when()
          .get("/books")
          .then()
          .statusCode(200)
          .body("$", empty());
    }

    // Unhappy path
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
    void getUserBooks_whenListEmpty_thenReturnEmptyListAnd200() {
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
    void getUserBooks_whenUnauthenticated_thenReturn401AndNoBody() {
      given()
          .when()
          .get("/books")
          .then()
          .statusCode(401)
          .body(emptyOrNullString());
    }
  }

  private User createUser() {
    Optional<Role> role = this.roleRepository.findByName("ROLE_USER");

    return this.userRepository.save(new User("jurgis@inbox.lt", passwordEncoder.encode("123456"),
        List.of(role.orElseThrow()), null));
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

  private int findUserBookIdByUserIdAndBookTitle(long id, String title) {
    return this.userBookRepository.findByUserIdAndBookTitle(id, title)
        .orElseThrow()
        .getId()
        .intValue();
  }

  private int findBookIdByTitle(String title) {
    return this.bookRepository.findByTitle(title)
        .orElseThrow()
        .getId()
        .intValue();
  }
}