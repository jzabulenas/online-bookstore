// package lt.techin.bookreservationapp;
//
// import static
// org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
// import org.junit.jupiter.api.Test;
// import org.springframework.ai.chat.client.ChatClient;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.context.annotation.Import;
// import org.springframework.http.MediaType;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;
//
// import lt.techin.bookreservationapp.book.BookController;
// import lt.techin.bookreservationapp.book.BookRepository;
// import lt.techin.bookreservationapp.role.RoleRepository;
// import lt.techin.bookreservationapp.security.SecurityConfig;
// import lt.techin.bookreservationapp.user.UserRepository;
//
// @WebMvcTest(controllers = BookController.class)
// @Import(SecurityConfig.class)
// public class BookControllerTest {
//
//  @Autowired MockMvc mockMvc;
//  @Autowired ChatClient chatClient;
//  @MockitoBean BookRepository bookRepository;
//  @MockitoBean UserRepository userRepository;
//  @MockitoBean RoleRepository roleRepository;
//  @MockitoBean UserDetailsService userDetailsService;
//
//  // @MockitoBean BookRepository bookRepository;
//
//  //
//  // generateBooks
//  //
//
//
//  @Test
//  @WithMockUser(username = "jurgis@gmail.com")
//  void generateBooks_whenBookIsGenerated_return200AndListOfBooks() throws Exception {
//
//    this.mockMvc
//        .perform(
//            post("/generate-books")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("Gabagol")
//                .with(csrf()))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("length()").value(3));
//  }
// }
