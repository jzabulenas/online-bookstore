package lt.techin.bookreservationapp.end_to_end;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

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
import lt.techin.bookreservationapp.role.Role;
import lt.techin.bookreservationapp.role.RoleRepository;
import lt.techin.bookreservationapp.user.User;
import lt.techin.bookreservationapp.user.UserRepository;
import lt.techin.bookreservationapp.user.UserRequestDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerTest {

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

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost:" + port;
    this.userRepository.deleteAll();
  }

  @Nested
  class SignUpTests {

    @Test
    void signup_whenUserSignsUp_thenReturn201AndBody() throws JsonProcessingException {
      String csrfToken = getCsrfToken();
      String email = "jurgis@inbox.lt";

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(new ObjectMapper()
              .writeValueAsString(new UserRequestDTO(email, "12345678", List.of(1L))))
          .when()
          .post("/signup")
          .then()
          .statusCode(201)
          .body("$", aMapWithSize(3))
          .body("id", equalTo(findUserIdByEmail(email)))
          .body("email", equalTo(email))
          .body("roles", hasSize(1))
          .body("roles[0]", equalTo(1))
          .header("Location", equalTo("http://localhost:" + port + "/signup/"
              + findUserIdByEmail(email)));
    }

    // Unhappy path
    //
    //
    //
    //

    @Test
    void signup_whenEmailIsNull_thenReturn400AndBody() throws JsonProcessingException {
      String csrfToken = getCsrfToken();

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(new ObjectMapper()
              .writeValueAsString(new UserRequestDTO(null, "12345678", List.of(1L))))
          .when()
          .post("/signup")
          .then()
          .statusCode(400)
          .body("$", aMapWithSize(1))
          .body("email", equalTo("must not be null"));
    }

    @Test
    void signup_whenEmailIsTooShort_thenReturn400AndBody() throws JsonProcessingException {
      String csrfToken = getCsrfToken();

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(new ObjectMapper()
              .writeValueAsString(new UserRequestDTO("f@b.c", "12345678", List.of(1L))))
          .when()
          .post("/signup")
          .then()
          .statusCode(400)
          .body("$", aMapWithSize(1))
          .body("email", equalTo("Email must be at least 7 characters long"));
    }

    @Test
    void signup_whenEmailLocalPartIsTooLong_thenReturn400AndBody() throws JsonProcessingException {
      String csrfToken = getCsrfToken();

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(new ObjectMapper()
              .writeValueAsString(new UserRequestDTO(
                  "ivctsadyhqcfxzjinykxemzadbyajutuqzawknkckrgbzcjlwgufbrcycrdicezrv@gmail.com",
                  "12345678", List.of(1L))))
          .when()
          .post("/signup")
          .then()
          .statusCode(400)
          .body("$", aMapWithSize(1))
          .body("email", equalTo("must be a well-formed email address"));
    }

    @Test
    void signup_whenEmailDomainPartIsTooLong_thenReturn400AndBody() throws JsonProcessingException {
      String csrfToken = getCsrfToken();

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(new ObjectMapper()
              .writeValueAsString(new UserRequestDTO(
                  "jurgis@ivctsadyhqcfxzjinykxemzadbyajutuqzawknkckrgbzcjlwgufbrcycrdicegw.com",
                  "12345678", List.of(1L))))
          .when()
          .post("/signup")
          .then()
          .statusCode(400)
          .body("$", aMapWithSize(1))
          .body("email", equalTo("must be a well-formed email address"));
    }

    @Test
    void signup_whenEmailLocalPartAndDomainPartIsTooLong_thenReturn400AndBody()
        throws JsonProcessingException {
      String csrfToken = getCsrfToken();

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(new ObjectMapper()
              .writeValueAsString(new UserRequestDTO(
                  "ivctsadyhqcfxzjinykxemzadbyajutuqzawknkckrgbzcjlwgufbrcycrdicezrrsdfsdfse@ivctsadyhqcfxzjinykxemzadbyajutuqzawknkckrgbzcjlwgufbrcycrdicegwasdasde.com",
                  "12345678", List.of(1L))))
          .when()
          .post("/signup")
          .then()
          .statusCode(400)
          .body("$", aMapWithSize(1))
          .body("email", equalTo("must be a well-formed email address"));
    }

    @Test
    void signup_whenEmailAlreadyExists_thenReturn400AndBody()
        throws JsonProcessingException {
      String csrfToken = getCsrfToken();
      String email = "jurgis@inbox.lt";
      String password = "12345678";
      Role role = roleRepository.findByName("ROLE_USER").orElseThrow();
      userRepository.save(new User(email, password, List.of(role), null));

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(new ObjectMapper()
              .writeValueAsString(new UserRequestDTO(
                  email, password, List.of(1L))))
          .when()
          .post("/signup")
          .then()
          .statusCode(400)
          .body("$", aMapWithSize(1))
          .body("email", equalTo("Already exists"));
    }

    // TODO: should I test combinations? That is, for example, email and password is
    // null.
  }

  private String getCsrfToken() {
    Response csrfResponse = given()
        .when()
        .get("/open")
        .then()
        .extract()
        .response();

    return csrfResponse.cookie("XSRF-TOKEN");
  }

  private int findUserIdByEmail(String email) {
    return this.userRepository.findByEmail(email)
        .orElseThrow()
        .getId()
        .intValue();
  }
}
