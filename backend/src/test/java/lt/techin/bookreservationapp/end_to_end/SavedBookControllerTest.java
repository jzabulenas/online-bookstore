package lt.techin.bookreservationapp.end_to_end;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

import io.restassured.RestAssured;
import lt.techin.bookreservationapp.entities.SavedBook;
import lt.techin.bookreservationapp.entities.User;
import lt.techin.bookreservationapp.repositories.SavedBookRepository;
import lt.techin.bookreservationapp.security.SecurityConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(SecurityConfig.class)
// @ActiveProfiles("test")
@AutoConfigureMockMvc
class SavedBookControllerTest {

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

  @Autowired SavedBookRepository savedBookRepository;

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost:" + port;
    savedBookRepository.deleteAll();
  }

  @Test
  @WithMockUser
  void shouldGetAllCustomers() throws Exception {

    this.savedBookRepository.save(new SavedBook("hello", new User()));

    //    given()
    //        .contentType(ContentType.JSON)
    //        .when()
    //        .get("/api/customers")
    //        .then()
    //        .statusCode(200)
    //        .body(".", hasSize(2));

    this.mockMvc
        .perform(get("/saved-books"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("length()").value(2));
  }
}
