package lt.techin.bookreservationapp;

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

  @LocalServerPort private Integer port;

  static MariaDBContainer<?> mariaDB =
      new MariaDBContainer<>(DockerImageName.parse("mariadb:11.4"));

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

  @Autowired private UserRepository userRepository;
  @Autowired private RoleRepository roleRepository;

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost:" + this.port;
    this.userRepository.deleteAll();
  }

  @Nested
  class SignUpTests {

    @Test
    void signup_whenUserSignsUp_thenReturn201AndBody() throws JsonProcessingException {
      String csrfToken = UserControllerTest.this.getCsrfToken();
      String email = "jurgis@inbox.lt";

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(
              new ObjectMapper()
                  .writeValueAsString(new UserRequestDTO(email, "r9$CbHEaGXLUsP", List.of(1L))))
          .when()
          .post("/signup")
          .then()
          .statusCode(201)
          .body("$", aMapWithSize(3))
          .body("id", equalTo(UserControllerTest.this.findUserIdByEmail(email)))
          .body("email", equalTo(email))
          .body("roles", hasSize(1))
          .body("roles[0]", equalTo(1))
          .header(
              "Location",
              equalTo(
                  "http://localhost:"
                      + UserControllerTest.this.port
                      + "/signup/"
                      + UserControllerTest.this.findUserIdByEmail(email)));
    }

    @Test
    void signup_whenUserSignsUpWithAsBigPasswordAsPossible_thenReturn201AndBody()
        throws JsonProcessingException {
      String csrfToken = UserControllerTest.this.getCsrfToken();
      String email = "jurgis@inbox.lt";

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(
              new ObjectMapper()
                  .writeValueAsString(
                      new UserRequestDTO(
                          email,
                          // This is 64 characters
                          "metyjwgaqakvjdrbpqsoywhrqzpesbrtsbtqfseffbivpfsaaihttjnjbmrbexbp",
                          List.of(1L))))
          .when()
          .post("/signup")
          .then()
          .statusCode(201)
          .body("$", aMapWithSize(3))
          .body("id", equalTo(UserControllerTest.this.findUserIdByEmail(email)))
          .body("email", equalTo(email))
          .body("roles", hasSize(1))
          .body("roles[0]", equalTo(1))
          .header(
              "Location",
              equalTo(
                  "http://localhost:"
                      + UserControllerTest.this.port
                      + "/signup/"
                      + UserControllerTest.this.findUserIdByEmail(email)));
    }

    // Unhappy path
    //
    //
    //
    //

    @Test
    void signup_whenEmailIsNull_thenReturn400AndBody() throws JsonProcessingException {
      String csrfToken = UserControllerTest.this.getCsrfToken();

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(
              new ObjectMapper()
                  .writeValueAsString(new UserRequestDTO(null, "r9$CbHEaGXLUsP", List.of(1L))))
          .when()
          .post("/signup")
          .then()
          .statusCode(400)
          .body("$", aMapWithSize(1))
          .body("email", equalTo("must not be null"));
    }

    @Test
    void signup_whenEmailIsTooShort_thenReturn400AndBody() throws JsonProcessingException {
      String csrfToken = UserControllerTest.this.getCsrfToken();

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(
              new ObjectMapper()
                  .writeValueAsString(new UserRequestDTO("f@b.c", "r9$CbHEaGXLUsP", List.of(1L))))
          .when()
          .post("/signup")
          .then()
          .statusCode(400)
          .body("$", aMapWithSize(1))
          .body("email", equalTo("Email must be at least 7 characters long"));
    }

    @Test
    void signup_whenEmailLocalPartIsTooLong_thenReturn400AndBody() throws JsonProcessingException {
      String csrfToken = UserControllerTest.this.getCsrfToken();

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(
              new ObjectMapper()
                  .writeValueAsString(
                      new UserRequestDTO(
                          "ivctsadyhqcfxzjinykxemzadbyajutuqzawknkckrgbzcjlwgufbrcycrdicezrv@gmail.com",
                          "r8@D^6PCg7&3Zn",
                          List.of(1L))))
          .when()
          .post("/signup")
          .then()
          .statusCode(400)
          .body("$", aMapWithSize(1))
          .body("email", equalTo("must be a well-formed email address"));
    }

    @Test
    void signup_whenEmailDomainPartIsTooLong_thenReturn400AndBody() throws JsonProcessingException {
      String csrfToken = UserControllerTest.this.getCsrfToken();

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(
              new ObjectMapper()
                  .writeValueAsString(
                      new UserRequestDTO(
                          "jurgis@ivctsadyhqcfxzjinykxemzadbyajutuqzawknkckrgbzcjlwgufbrcycrdicegw.com",
                          "r9$CbHEaGXLUsP",
                          List.of(1L))))
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
      String csrfToken = UserControllerTest.this.getCsrfToken();

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(
              new ObjectMapper()
                  .writeValueAsString(
                      new UserRequestDTO(
                          "ivctsadyhqcfxzjinykxemzadbyajutuqzawknkckrgbzcjlwgufbrcycrdicezrrsdfsdfse@ivctsadyhqcfxzjinykxemzadbyajutuqzawknkckrgbzcjlwgufbrcycrdicegwasdasde.com",
                          "r8@D^6PCg7&3Zn",
                          List.of(1L))))
          .when()
          .post("/signup")
          .then()
          .statusCode(400)
          .body("$", aMapWithSize(1))
          .body("email", equalTo("must be a well-formed email address"));
    }

    @Test
    void signup_whenEmailAlreadyExists_thenReturn400AndBody() throws JsonProcessingException {
      String csrfToken = UserControllerTest.this.getCsrfToken();
      String email = "jurgis@inbox.lt";
      String password = "r9$CbHEaGXLUsP";
      Role role = UserControllerTest.this.roleRepository.findByName("ROLE_USER").orElseThrow();
      UserControllerTest.this.userRepository.save(
          new User(email, password, true, null, List.of(role), null));

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(
              new ObjectMapper()
                  .writeValueAsString(new UserRequestDTO(email, password, List.of(1L))))
          .when()
          .post("/signup")
          .then()
          .statusCode(400)
          .body("$", aMapWithSize(1))
          .body("email", equalTo("Such email address is already in use"));
    }

    // Password
    //
    //
    //
    //

    @Test
    void signup_whenPasswordIsNull_shouldReturn400AndBody() throws JsonProcessingException {
      String csrfToken = UserControllerTest.this.getCsrfToken();

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(
              new ObjectMapper()
                  .writeValueAsString(new UserRequestDTO("jurgis@inbox.lt", null, List.of(1L))))
          .when()
          .post("/signup")
          .then()
          .statusCode(400)
          .body("$", aMapWithSize(1))
          .body("password", equalTo("must not be null"));
    }

    @Test
    void signup_whenPasswordIsTooShort_shouldReturn400AndBody() throws JsonProcessingException {
      String csrfToken = UserControllerTest.this.getCsrfToken();

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(
              new ObjectMapper()
                  .writeValueAsString(
                      new UserRequestDTO("jurgis@inbox.lt", "grxnqdgnsqbqj", List.of(1L))))
          .when()
          .post("/signup")
          .then()
          .statusCode(400)
          .body("$", aMapWithSize(1))
          .body("password", equalTo("size must be between 14 and 64"));
    }

    @Test
    void signup_whenPasswordIsTooLong_shouldReturn400AndBody() throws JsonProcessingException {
      String csrfToken = UserControllerTest.this.getCsrfToken();

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(
              new ObjectMapper()
                  .writeValueAsString(
                      new UserRequestDTO(
                          "jurgis@inbox.lt",
                          // This is 65 characters
                          "metyjwgaqakvjdrbpqsoywhrqzpesbrtsbtqfseffbivpfsaaihttjnjbmrbexbpr",
                          List.of(1L))))
          .when()
          .post("/signup")
          .then()
          .statusCode(400)
          .body("$", aMapWithSize(1))
          .body("password", equalTo("size must be between 14 and 64"));
    }

    @Test
    void signup_whenPasswordIsFoundToBeCompromised_thenReturn400AndBody()
        throws JsonProcessingException {
      String csrfToken = UserControllerTest.this.getCsrfToken();

      given()
          .cookie("XSRF-TOKEN", csrfToken)
          .header("X-XSRF-TOKEN", csrfToken)
          .contentType(ContentType.JSON)
          .body(
              new ObjectMapper()
                  .writeValueAsString(
                      new UserRequestDTO("jurgis@inbox.lt", "12345678912345", List.of(1L))))
          .when()
          .post("/signup")
          .then()
          .statusCode(400)
          .body("$", aMapWithSize(5))
          .body(
              "detail",
              equalTo(
                  "The provided password is compromised and cannot be used. Use something more unique"));
    }

    // @Test
    // void
    // signup_whenPasswordIsRightLengthButDoesNotContainUppercaseLetter_thenReturn400AndBody()
    // throws JsonProcessingException {
    // String csrfToken = UserControllerTest.this.getCsrfToken();
    //
    // given()
    // .cookie("XSRF-TOKEN", csrfToken)
    // .header("X-XSRF-TOKEN", csrfToken)
    // .contentType(ContentType.JSON)
    // .body(
    // new ObjectMapper()
    // .writeValueAsString(
    // new UserRequestDTO("jurgis@inbox.lt", "oif3r2t^x^k%n9", List.of(1L))))
    // .when()
    // .post("/signup")
    // .then()
    // .statusCode(400)
    // .body("$", aMapWithSize(1))
    // .body(
    // "password",
    // equalTo(
    // "Must contain at least one uppercase and lowercase letter, number and any of
    // these symbols: !@#$%^&*"));
    // }

    // @Test
    // void
    // signup_whenPasswordIsRightLengthButDoesNotContainLowercaseLetter_thenReturn400AndBody()
    // throws JsonProcessingException {
    // String csrfToken = UserControllerTest.this.getCsrfToken();
    //
    // given()
    // .cookie("XSRF-TOKEN", csrfToken)
    // .header("X-XSRF-TOKEN", csrfToken)
    // .contentType(ContentType.JSON)
    // .body(
    // new ObjectMapper()
    // .writeValueAsString(
    // new UserRequestDTO("jurgis@inbox.lt", "$EES#!CZ28L#$2", List.of(1L))))
    // .when()
    // .post("/signup")
    // .then()
    // .statusCode(400)
    // .body("$", aMapWithSize(1))
    // .body(
    // "password",
    // equalTo(
    // "Must contain at least one uppercase and lowercase letter, number and any of
    // these symbols: !@#$%^&*"));
    // }

    // @Test
    // void
    // signup_whenPasswordIsRightLengthButDoesNotContainNumber_thenReturn400AndBody()
    // throws JsonProcessingException {
    // String csrfToken = UserControllerTest.this.getCsrfToken();
    //
    // given()
    // .cookie("XSRF-TOKEN", csrfToken)
    // .header("X-XSRF-TOKEN", csrfToken)
    // .contentType(ContentType.JSON)
    // .body(
    // new ObjectMapper()
    // .writeValueAsString(
    // new UserRequestDTO("jurgis@inbox.lt", "QFUWmF$YsrBS#h", List.of(1L))))
    // .when()
    // .post("/signup")
    // .then()
    // .statusCode(400)
    // .body("$", aMapWithSize(1))
    // .body(
    // "password",
    // equalTo(
    // "Must contain at least one uppercase and lowercase letter, number and any of
    // these symbols: !@#$%^&*"));
    // }

    // @Test
    // void
    // signup_whenPasswordIsRightLengthButDoesNotContainSpecialSymbol_thenReturn400AndBody()
    // throws JsonProcessingException {
    // String csrfToken = UserControllerTest.this.getCsrfToken();
    //
    // given()
    // .cookie("XSRF-TOKEN", csrfToken)
    // .header("X-XSRF-TOKEN", csrfToken)
    // .contentType(ContentType.JSON)
    // .body(
    // new ObjectMapper()
    // .writeValueAsString(
    // new UserRequestDTO("jurgis@inbox.lt", "TSK3bgRXkduc66", List.of(1L))))
    // .when()
    // .post("/signup")
    // .then()
    // .statusCode(400)
    // .body("$", aMapWithSize(1))
    // .body(
    // "password",
    // equalTo(
    // "Must contain at least one uppercase and lowercase letter, number and any of
    // these symbols: !@#$%^&*"));
    // }

    // TODO: should I test combinations? That is, for example, email and password is
    // null.
  }

  private String getCsrfToken() {
    Response csrfResponse = given().when().get("/open").then().extract().response();

    return csrfResponse.cookie("XSRF-TOKEN");
  }

  private int findUserIdByEmail(String email) {
    return this.userRepository.findByEmail(email).orElseThrow().getId().intValue();
  }
}
