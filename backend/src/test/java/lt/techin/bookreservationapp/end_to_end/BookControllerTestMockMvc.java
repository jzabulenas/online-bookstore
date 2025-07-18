//package lt.techin.bookreservationapp.end_to_end;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.hamcrest.Matchers.hasSize;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.testcontainers.containers.MariaDBContainer;
//import org.testcontainers.utility.DockerImageName;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import io.restassured.RestAssured;
//import lt.techin.bookreservationapp.book.Book;
//import lt.techin.bookreservationapp.book.BookRepository;
//import lt.techin.bookreservationapp.book.BookRequestDTO;
//import lt.techin.bookreservationapp.book.MessageRequestDTO;
//import lt.techin.bookreservationapp.role.Role;
//import lt.techin.bookreservationapp.role.RoleRepository;
//import lt.techin.bookreservationapp.security.SecurityConfig;
//import lt.techin.bookreservationapp.user.User;
//import lt.techin.bookreservationapp.user.UserRepository;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Import(SecurityConfig.class)
//@ActiveProfiles("test")
//@AutoConfigureMockMvc
//class BookControllerTestMockMvc {
//
//  @Autowired
//  BookRepository bookRepository;
//  @Autowired
//  UserRepository userRepository;
//  @Autowired
//  RoleRepository roleRepository;
//  @Autowired
//  PasswordEncoder passwordEncoder;
//
//  @LocalServerPort
//  private Integer port;
//
//  @Autowired
//  private MockMvc mockMvc;
//
//  static MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>(
//      DockerImageName.parse("mariadb:10.11"));
//
//  @BeforeAll
//  static void beforeAll() {
//    mariaDBContainer.start();
//  }
//
//  @AfterAll
//  static void afterAll() {
//    mariaDBContainer.stop();
//  }
//
//  @DynamicPropertySource
//  static void configureProperties(DynamicPropertyRegistry registry) {
//    registry.add("spring.datasource.url", mariaDBContainer::getJdbcUrl);
//    registry.add("spring.datasource.username", mariaDBContainer::getUsername);
//    registry.add("spring.datasource.password", mariaDBContainer::getPassword);
//  }
//
//  @BeforeEach
//  void setUp() {
//    RestAssured.baseURI = "http://localhost:" + port;
//    this.bookRepository.deleteAll();
//    this.userRepository.deleteAll();
//  }
//
//  // generateBooks
//  //
//  //
//  //
//  //
//  //
//  //
//  //
//  //
//  //
//
//  @Test
//  @WithMockUser(username = "jurgis@gmail.com")
//  void generateBooks_whenBookIsGenerated_return200AndListOfBooks() throws Exception {
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    this.mockMvc
//        .perform(post("/generate-books")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(new MessageRequestDTO("Gabagol")))
//            .with(csrf()))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("length()").value(1))
//        .andExpect(jsonPath("result", hasSize(3)));
//  }
//
//  @Test
//  @WithMockUser(username = "jurgis@gmail.com")
//  void generateBooks_whenMessageIsNull_return400AndMessage() throws Exception {
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    this.mockMvc
//        .perform(post("/generate-books")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(new MessageRequestDTO(null)))
//            .with(csrf()))
//        .andExpect(status().isBadRequest())
//        .andExpect(jsonPath("message").value("must not be null"))
//        .andExpect(jsonPath("length()").value(1));
//  }
//
//  @Test
//  @WithMockUser(username = "jurgis@gmail.com")
//  void generateBooks_whenMessageIsTooShort_return400AndMessage() throws Exception {
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    this.mockMvc
//        .perform(post("/generate-books")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(new MessageRequestDTO("Fe")))
//            .with(csrf()))
//        .andExpect(status().isBadRequest())
//        .andExpect(jsonPath("message").value("size must be between 5 and 100"))
//        .andExpect(jsonPath("length()").value(1));
//  }
//
//  @Test
//  @WithMockUser(username = "jurgis@gmail.com")
//  void generateBooks_whenMessageIsTooLong_return400AndMessage() throws Exception {
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    this.mockMvc
//        .perform(post("/generate-books")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(new MessageRequestDTO(
//                "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor."
//                    + " Aenean mj")))
//            .with(csrf()))
//        .andExpect(status().isBadRequest())
//        .andExpect(jsonPath("message").value("size must be between 5 and 100"))
//        .andExpect(jsonPath("length()").value(1));
//  }
//
//  @Test
//  void generateBooks_whenUnauthenticated_thenReturn401() throws Exception {
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    this.mockMvc
//        .perform(post("/generate-books")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(new MessageRequestDTO("Gabagol")))
//            .with(csrf()))
//        .andExpect(status().isUnauthorized());
//
//    // Ask LLM why without I get 403?
//    // .with(csrf))
//
//    // RestAssured equivalent
//    // given()
//    // .contentType(ContentType.JSON)
//    // .body(objectMapper.writeValueAsString(new MessageRequestDTO("Gabagol")))
//    // .redirects()
//    // .follow(true)
//    // .when()
//    // .post("/generate-books")
//    // .then()
//    // .statusCode(302);
//  }
//
//  // saveBook
//  //
//  //
//  //
//  //
//  //
//  //
//  //
//  //
//  //
//
//  @Test
//  @WithMockUser(username = "jurgis@gmail.com")
//  void saveBook_whenBookIsSaved_return201() throws Exception {
//
//    Optional<Role> role = this.roleRepository.findByName("ROLE_USER");
//
//    User user = this.userRepository.save(new User(
//        "jurgis@gmail.com",
//        passwordEncoder.encode("WKXu63PxD3bHYB"),
//        List.of(role.orElseThrow())));
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    this.mockMvc
//        .perform(post("/books")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper
//                .writeValueAsString(new BookRequestDTO("Edward III: The Perfect King")))
//            .with(csrf()))
//        .andExpect(status().isCreated())
//        .andExpect(jsonPath("length()").value(2))
//        .andExpect(jsonPath("title").value("Edward III: The Perfect King"))
//        .andExpect(jsonPath("userId").value(user.getId()))
//        .andExpect(header().string("Location", containsString("/books/" + user.getId())));
//  }
//
//  @Test
//  void saveBook_whenUnauthenticatedCalls_thenReturn401() throws Exception {
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    this.mockMvc
//        .perform(post("/books")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper
//                .writeValueAsString(new BookRequestDTO("Edward III: The Perfect King")))
//            .with(csrf()))
//        .andExpect(status().isUnauthorized());
//  }
//
//  @Test
//  @WithMockUser(username = "jurgis@gmail.com")
//  void saveBook_whenTitleAlreadyExistsForUser_thenReturn400AndMessage() throws Exception {
//
//    Optional<Role> role = this.roleRepository.findByName("ROLE_USER");
//
//    User user = this.userRepository.save(new User(
//        "jurgis@gmail.com",
//        passwordEncoder.encode("WKXu63PxD3bHYB"),
//        List.of(role.orElseThrow())));
//
//    this.bookRepository.save(new Book("Edward III: The Perfect King", user));
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    this.mockMvc
//        .perform(post("/books")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper
//                .writeValueAsString(new BookRequestDTO("Edward III: The Perfect King")))
//            .with(csrf()))
//        .andExpect(status().isBadRequest())
//        .andExpect(jsonPath("length()").value(1))
//        .andExpect(jsonPath("title").value("Already exists"));
//  }
//
//  @Test
//  @WithMockUser(username = "jurgis@gmail.com")
//  void saveBook_whenTitleAlreadyExistsForOtherUser_thenReturn201AndMessage() throws Exception {
//
//    Optional<Role> role = this.roleRepository.findByName("ROLE_USER");
//
//    User otherUser = this.userRepository.save(new User(
//        "antanas@gmail.com",
//        passwordEncoder.encode("6BRMrh85uPWdMj"),
//        List.of(role.orElseThrow())));
//
//    String bookName = "Edward III: The Perfect King";
//
//    User user = this.userRepository.save(new User(
//        "jurgis@gmail.com",
//        passwordEncoder.encode("WKXu63PxD3bHYB"),
//        List.of(role.orElseThrow())));
//
//    this.bookRepository.save(new Book(bookName, otherUser));
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    this.mockMvc
//        .perform(post("/books")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(new BookRequestDTO(bookName)))
//            .with(csrf()))
//        .andExpect(status().isCreated())
//        .andExpect(jsonPath("length()").value(2))
//        .andExpect(jsonPath("title").value(bookName))
//        .andExpect(jsonPath("userId").value(user.getId()))
//        .andExpect(header().string("Location", containsString("/books/" + user.getId())));
//  }
//
//  // getBooks
//  //
//  //
//  //
//  //
//  //
//  //
//  //
//  //
//  //
//
//  @Test
//  @WithMockUser(username = "jurgis@gmail.com")
//  void getBooks_whenCalled_returnBooksAnd200() throws Exception {
//
//    Optional<Role> role = this.roleRepository.findByName("ROLE_USER");
//
//    User user = this.userRepository.save(new User(
//        "jurgis@gmail.com",
//        passwordEncoder.encode("WKXu63PxD3bHYB"),
//        List.of(role.orElseThrow())));
//
//    this.bookRepository.save(new Book("Edward III: The Perfect King", user));
//    this.bookRepository.save(new Book(
//        "The Greatest Traitor: The Life of Sir Roger Mortimer, Ruler of England 1327–1330",
//        user));
//
//    this.mockMvc
//        .perform(get("/books"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("length()").value(2))
//        .andExpect(jsonPath("[0].title").value("Edward III: The Perfect King"))
//        .andExpect(jsonPath("[0].length()").value(1))
//        .andExpect(jsonPath("[1].title")
//            .value("The Greatest Traitor: The Life of Sir Roger Mortimer, Ruler of England 1327–1330"))
//        .andExpect(jsonPath("[1].length()").value(1));
//  }
//
//  @Test
//  void getBooks_whenCalledUnauthenticated_thenReturn401() throws Exception {
//
//    Optional<Role> role = this.roleRepository.findByName("ROLE_USER");
//
//    User user = this.userRepository.save(new User(
//        "jurgis@gmail.com",
//        passwordEncoder.encode("WKXu63PxD3bHYB"),
//        List.of(role.orElseThrow())));
//
//    this.bookRepository.save(new Book("Edward III: The Perfect King", user));
//    this.bookRepository.save(new Book(
//        "The Greatest Traitor: The Life of Sir Roger Mortimer, Ruler of England 1327–1330",
//        user));
//
//    this.mockMvc
//        .perform(get("/books"))
//        .andExpect(status().isUnauthorized())
//        .andExpect(content().string(""));
//  }
//
//  @Test
//  @WithMockUser(username = "jurgis@gmail.com")
//  void getBooks_whenCalledAndListEmpty_returnEmptyListAnd200() throws Exception {
//
//    Optional<Role> role = this.roleRepository.findByName("ROLE_USER");
//
//    User user = this.userRepository.save(new User(
//        "jurgis@gmail.com",
//        passwordEncoder.encode("WKXu63PxD3bHYB"),
//        List.of(role.orElseThrow())));
//
//    this.mockMvc
//        .perform(get("/books"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$").isArray())
//        .andExpect(jsonPath("$").isEmpty());
//  }
//}
