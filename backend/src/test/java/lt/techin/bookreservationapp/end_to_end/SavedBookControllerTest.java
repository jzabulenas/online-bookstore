package lt.techin.bookreservationapp.end_to_end;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lt.techin.bookreservationapp.repositories.SavedBookRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SavedBookControllerTest {

  @LocalServerPort private Integer port;

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

  @Autowired SavedBookRepository savedBookRepository;

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost:" + port;
    savedBookRepository.deleteAll();
  }

  @Test
  @WithMockUser
  void shouldGetAllCustomers() {

    given()
        .contentType(ContentType.JSON)
        .when()
        .get("/api/customers")
        .then()
        .statusCode(200)
        .body(".", hasSize(2));
  }
}
