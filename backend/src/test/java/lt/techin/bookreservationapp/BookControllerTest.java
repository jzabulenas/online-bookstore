// package lt.techin.bookreservationapp;
//
// import static org.hamcrest.Matchers.containsInAnyOrder;
// import static org.hamcrest.Matchers.hasSize;
// import static org.hamcrest.Matchers.is;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyInt;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.BDDMockito.given;
// import static org.mockito.BDDMockito.then;
// import static org.mockito.Mockito.never;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
// import java.time.LocalDate;
// import java.util.List;
// import java.util.Set;
//
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.context.annotation.Import;
// import org.springframework.http.MediaType;
// import org.springframework.security.test.context.support.WithUserDetails;
// import org.springframework.test.web.servlet.MockMvc;
//
// import lt.techin.bookreservationapp.controllers.BookController;
// import lt.techin.bookreservationapp.entities.Book;
// import lt.techin.bookreservationapp.entities.Category;
// import lt.techin.bookreservationapp.security.SecurityConfig;
// import lt.techin.bookreservationapp.services.BookService;
// import lt.techin.bookreservationapp.services.CategoryService;
//
// @WebMvcTest(controllers = BookController.class)
// @Import(SecurityConfig.class)
// public class BookControllerTest {
//
//  @Autowired private MockMvc mockMvc;
//
//  @MockBean private BookService bookService;
//
//  @MockBean private CategoryService categoryService;
//
//  @Test
//  @WithUserDetails
//  void getBooks_whenAuthenticatedCalls_thenReturnBooksAnd200() throws Exception {
//    Book book1 = createTestBook1();
//    Book book2 = createTestBook2();
//
//    given(bookService.findAllBooks()).willReturn(List.of(book1, book2));
//
//    mockMvc
//        .perform(get("/books"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$", hasSize(2)))
//        .andExpect(jsonPath("[0].author").value(book1.getAuthor()))
//        .andExpect(
//            jsonPath("[0].categories[0].name").value(containsInAnyOrder(book1.getCategories())))
//        .andExpect(
//            jsonPath("[0].categories[1].name").value(containsInAnyOrder(book1.getCategories())))
//        .andExpect(
//            jsonPath("[0].categories[2].name").value(containsInAnyOrder(book1.getCategories())))
//        .andExpect(jsonPath("[0].description").value(book1.getDescription()))
//        .andExpect(jsonPath("[0].isbn").value(book1.getIsbn()))
//        .andExpect(jsonPath("[0].publicationDate").value(book1.getPublicationDate() + ""))
//        .andExpect(jsonPath("[0].title").value(book1.getTitle()))
//        .andExpect(jsonPath("[0].pictureUrl").value(book1.getPictureUrl()))
//        .andExpect(jsonPath("[0].pages").value(book1.getPages()))
//        .andExpect(jsonPath("[0].language").value(book1.getLanguage()))
//        // Second book
//        .andExpect(jsonPath("[1].author").value(book2.getAuthor()))
//        .andExpect(
//            jsonPath("[1].categories[0].name").value(containsInAnyOrder(book1.getCategories())))
//        .andExpect(
//            jsonPath("[1].categories[1].name").value(containsInAnyOrder(book1.getCategories())))
//        .andExpect(jsonPath("[1].description").value(book2.getDescription()))
//        .andExpect(jsonPath("[1].isbn").value(book2.getIsbn()))
//        .andExpect(jsonPath("[1].publicationDate").value(book2.getPublicationDate() + ""))
//        .andExpect(jsonPath("[1].title").value(book2.getTitle()))
//        .andExpect(jsonPath("[1].pictureUrl").value(book2.getPictureUrl()))
//        .andExpect(jsonPath("[1].pages").value(book2.getPages()))
//        .andExpect(jsonPath("[1].language").value(book2.getLanguage()));
//
//    then(bookService).should().findAllBooks();
//  }
//
//  @Test
//  @WithUserDetails
//  void getBooks_whenAuthenticatedCallsAndListEmpty_thenReturnResponseAnd404() throws Exception {
//    given(bookService.findAllBooks()).willReturn(List.of());
//
//    mockMvc
//        .perform(get("/books"))
//        .andExpect(status().isNotFound())
//        .andExpect(jsonPath("message").value("No books found"));
//
//    then(bookService).should().findAllBooks();
//  }
//
//  @Test
//  void getBooks_whenUnauthenitcatedCalls_thenReturn401() throws Exception {
//    mockMvc.perform(get("/books")).andExpect(status().isUnauthorized());
//
//    then(bookService).should(never()).findAllBooks();
//  }
//
//  // getBook
//
//  @Test
//  @WithUserDetails
//  void getBook_whenAuthenticatedCalls_thenReturnBookAnd200() throws Exception {
//    Book book1 = createTestBook1();
//    int id = 16;
//
//    given(bookService.findBookById(id)).willReturn(book1);
//
//    mockMvc
//        .perform(get("/books/{id}", id))
//        .andExpect(status().isOk())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//        .andExpect(jsonPath("author", is(book1.getAuthor())))
//        .andExpect(jsonPath("categories[0].name", is("jurgis")))
//        .andExpect(jsonPath("categories[1].name", is("antanas")))
//        .andExpect(jsonPath("categories[2].name", is("aloyzas")))
//        .andExpect(jsonPath("description", is(book1.getDescription())))
//        .andExpect(jsonPath("isbn", is(book1.getIsbn())))
//        .andExpect(jsonPath("publicationDate", is(book1.getPublicationDate().toString())))
//        .andExpect(jsonPath("title", is(book1.getTitle())))
//        .andExpect(jsonPath("pictureUrl", is(book1.getPictureUrl())))
//        .andExpect(jsonPath("pages", is(book1.getPages())))
//        .andExpect(jsonPath("language", is(book1.getLanguage())));
//
//    then(bookService).should().findBookById(id);
//  }
//
//  @Test
//  @WithUserDetails
//  void getBook_whenBookIsNonExistent_thenReturn404() throws Exception {
//    int id = 81;
//
//    given(bookService.findBookById(id)).willReturn(null);
//
//    mockMvc
//        .perform(get("/books/{id}", id))
//        .andExpect(status().isNotFound())
//        .andExpect(jsonPath("message", is("Book with Id " + id + " not found")));
//
//    then(bookService).should().findBookById(id);
//  }
//
//  @Test
//  void getBook_whenUnauthenticatedCalls_thenReturn401() throws Exception {
//    int id = 78;
//
//    mockMvc.perform(get("/books/{id}", id)).andExpect(status().isUnauthorized());
//
//    then(bookService).should(never()).findBookById(id);
//  }
//
//  // addBook
//  @Test
//  @WithUserDetails
//  void addBook_whenAddBook_thenReturnBodyAnd201() throws Exception {
//    Book book1 = createTestBook1();
//    int categoryId1 = 89;
//    int categoryId2 = 20;
//    int categoryId3 = 46;
//    given(bookService.existsBookByTitle(book1.getTitle())).willReturn(false);
//    given(bookService.existsBookByIsbn(book1.getIsbn())).willReturn(false);
//    given(categoryService.findCategoryById(categoryId1)).willReturn(null);
//    given(categoryService.findCategoryById(categoryId2)).willReturn(null);
//    given(categoryService.findCategoryById(categoryId3)).willReturn(null);
//    given(bookService.saveBook(any(Book.class))).willReturn(book1);
//
//    mockMvc
//        .perform(
//            post("/books")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(
//                    """
//        		{
//        		  "author": "%s",
//        		  "categories": [
//        		    {
//        		      "id": "%s"
//        		    },
//        		    {
//        		      "id" : "%s"
//        		    },
//        		    {
//        		      "id": "%s"
//        		    }
//        		  ],
//        		  "description": "%s",
//        		  "isbn": "%s",
//        		  "publicationDate": "%s",
//        		  "title": "%s",
//        		  "pictureUrl": "%s",
//        		  "pages": "%s",
//        		  "language": "%s"
//        		}
//        		"""
//                        .formatted(
//                            book1.getAuthor(),
//                            categoryId1,
//                            categoryId2,
//                            categoryId3,
//                            book1.getDescription(),
//                            book1.getIsbn(),
//                            book1.getPublicationDate(),
//                            book1.getTitle(),
//                            book1.getPictureUrl(),
//                            book1.getPages(),
//                            book1.getLanguage()))
//                .accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isCreated())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//        .andExpect(jsonPath("author", is(book1.getAuthor())))
//        .andExpect(jsonPath("categories[0].name", is(null)))
//        .andExpect(jsonPath("categories[1].name", is(null)))
//        .andExpect(jsonPath("categories[2].name", is(null)))
//        .andExpect(jsonPath("description", is(book1.getDescription())))
//        .andExpect(jsonPath("isbn", is(book1.getIsbn())))
//        .andExpect(jsonPath("publicationDate", is(book1.getPublicationDate().toString())))
//        .andExpect(jsonPath("title", is(book1.getTitle())))
//        .andExpect(jsonPath("pictureUrl", is(book1.getPictureUrl())))
//        .andExpect(jsonPath("pages", is(book1.getPages())))
//        .andExpect(jsonPath("language", is(book1.getLanguage())));
//
//    then(bookService).should().existsBookByTitle(book1.getTitle());
//    then(bookService).should().existsBookByIsbn(book1.getIsbn());
//    then(categoryService).should().findCategoryById(categoryId1);
//    then(categoryService).should().findCategoryById(categoryId2);
//    then(categoryService).should().findCategoryById(categoryId3);
//    then(bookService).should().saveBook(any(Book.class));
//  }
//
//  @Test
//  @WithUserDetails
//  void addBook_whenBookExistsByTitle_thenReturnResponseBodyAnd400() throws Exception {
//    Book book1 = createTestBook1();
//    int categoryId1 = 89;
//    int categoryId2 = 20;
//    int categoryId3 = 46;
//    given(bookService.existsBookByTitle(book1.getTitle())).willReturn(true);
//
//    mockMvc
//        .perform(
//            post("/books")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(
//                    """
//	        		{
//	        		  "author": "%s",
//	        		  "categories": [
//	        		    {
//	        		      "id": "%s"
//	        		    },
//	        		    {
//	        		      "id" : "%s"
//	        		    },
//	        		    {
//	        		      "id": "%s"
//	        		    }
//	        		  ],
//	        		  "description": "%s",
//	        		  "isbn": "%s",
//	        		  "publicationDate": "%s",
//	        		  "title": "%s",
//	        		  "pictureUrl": "%s",
//	        		  "pages": "%s",
//	        		  "language": "%s"
//	        		}
//	        		"""
//                        .formatted(
//                            book1.getAuthor(),
//                            categoryId1,
//                            categoryId2,
//                            categoryId3,
//                            book1.getDescription(),
//                            book1.getIsbn(),
//                            book1.getPublicationDate(),
//                            book1.getTitle(),
//                            book1.getPictureUrl(),
//                            book1.getPages(),
//                            book1.getLanguage()))
//                .accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isBadRequest())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//        .andExpect(jsonPath("length()", is(1)))
//        .andExpect(jsonPath("title", is("Already exists")));
//
//    then(bookService).should().existsBookByTitle(book1.getTitle());
//    then(bookService).should(never()).existsBookByIsbn(anyString());
//    then(categoryService).should(never()).findCategoryById(anyInt());
//    then(categoryService).should(never()).findCategoryById(anyInt());
//    then(categoryService).should(never()).findCategoryById(anyInt());
//    then(bookService).should(never()).saveBook(any(Book.class));
//  }
//
//  @Test
//  @WithUserDetails
//  void addBook_whenBookExistsByIsbn_returnResponseBodyAnd400() throws Exception {
//    Book book1 = createTestBook1();
//    int categoryId1 = 89;
//    int categoryId2 = 20;
//    int categoryId3 = 46;
//    given(bookService.existsBookByTitle(book1.getTitle())).willReturn(false);
//    given(bookService.existsBookByIsbn(book1.getIsbn())).willReturn(true);
//
//    mockMvc
//        .perform(
//            post("/books")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(
//                    """
//		        		{
//		        		  "author": "%s",
//		        		  "categories": [
//		        		    {
//		        		      "id": "%s"
//		        		    },
//		        		    {
//		        		      "id" : "%s"
//		        		    },
//		        		    {
//		        		      "id": "%s"
//		        		    }
//		        		  ],
//		        		  "description": "%s",
//		        		  "isbn": "%s",
//		        		  "publicationDate": "%s",
//		        		  "title": "%s",
//		        		  "pictureUrl": "%s",
//		        		  "pages": "%s",
//		        		  "language": "%s"
//		        		}
//		        		"""
//                        .formatted(
//                            book1.getAuthor(),
//                            categoryId1,
//                            categoryId2,
//                            categoryId3,
//                            book1.getDescription(),
//                            book1.getIsbn(),
//                            book1.getPublicationDate(),
//                            book1.getTitle(),
//                            book1.getPictureUrl(),
//                            book1.getPages(),
//                            book1.getLanguage()))
//                .accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isBadRequest())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//        .andExpect(jsonPath("length()", is(1)))
//        .andExpect(jsonPath("isbn", is("Already exists")));
//
//    then(bookService).should().existsBookByTitle(book1.getTitle());
//    then(bookService).should().existsBookByIsbn(book1.getIsbn());
//    then(categoryService).should(never()).findCategoryById(categoryId1);
//    then(categoryService).should(never()).findCategoryById(categoryId2);
//    then(categoryService).should(never()).findCategoryById(categoryId3);
//    then(bookService).should(never()).saveBook(any(Book.class));
//  }
//
//  @Test
//  @WithUserDetails
//  void addBook_whenBookCategoriesAreDuplicate_thenReturnBodyAnd400() throws Exception {
//    Book book1 = createTestBook1();
//    int categoryId1 = 89;
//    int categoryId2 = 46;
//    given(bookService.existsBookByTitle(book1.getTitle())).willReturn(false);
//    given(bookService.existsBookByIsbn(book1.getIsbn())).willReturn(false);
//
//    mockMvc
//        .perform(
//            post("/books")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(
//                    """
//			        		{
//			        		  "author": "%s",
//			        		  "categories": [
//			        		    {
//			        		      "id": "%s"
//			        		    },
//			        		    {
//			        		      "id" : "%s"
//			        		    },
//			        		    {
//			        		      "id": "%s"
//			        		    }
//			        		  ],
//			        		  "description": "%s",
//			        		  "isbn": "%s",
//			        		  "publicationDate": "%s",
//			        		  "title": "%s",
//			        		  "pictureUrl": "%s",
//			        		  "pages": "%s",
//			        		  "language": "%s"
//			        		}
//			        		"""
//                        .formatted(
//                            book1.getAuthor(),
//                            categoryId1,
//                            categoryId1,
//                            categoryId2,
//                            book1.getDescription(),
//                            book1.getIsbn(),
//                            book1.getPublicationDate(),
//                            book1.getTitle(),
//                            book1.getPictureUrl(),
//                            book1.getPages(),
//                            book1.getLanguage()))
//                .accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isBadRequest())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//        .andExpect(jsonPath("length()", is(1)))
//        .andExpect(jsonPath("categories", is("Cannot be duplicate")));
//
//    then(bookService).should().existsBookByTitle(book1.getTitle());
//    then(bookService).should().existsBookByIsbn(book1.getIsbn());
//    then(categoryService).should().findCategoryById(categoryId1);
//    then(categoryService).should(never()).findCategoryById(categoryId2);
//    then(bookService).should(never()).saveBook(any(Book.class));
//  }
//
//  @Test
//  @WithUserDetails
//  void addBook_whenCategoriesIsNull_thenReturnBodyAnd400() throws Exception {
//    Book book1 = createTestBook1();
//    given(bookService.existsBookByTitle(book1.getTitle())).willReturn(false);
//    given(bookService.existsBookByIsbn(book1.getIsbn())).willReturn(false);
//
//    mockMvc
//        .perform(
//            post("/books")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(
//                    """
//				        		{
//				        		  "author": "%s",
//				        		  "description": "%s",
//				        		  "isbn": "%s",
//				        		  "publicationDate": "%s",
//				        		  "title": "%s",
//				        		  "pictureUrl": "%s",
//				        		  "pages": "%s",
//				        		  "language": "%s"
//				        		}
//				        		"""
//                        .formatted(
//                            book1.getAuthor(),
//                            book1.getDescription(),
//                            book1.getIsbn(),
//                            book1.getPublicationDate(),
//                            book1.getTitle(),
//                            book1.getPictureUrl(),
//                            book1.getPages(),
//                            book1.getLanguage()))
//                .accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isBadRequest())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//        .andExpect(jsonPath("length()", is(1)))
//        .andExpect(jsonPath("categories", is("Must not be null or empty")));
//
//    // The reason why these two are not called, is because annotations intercept
//    // the validation before it even gets to this
//    then(bookService).should(never()).existsBookByTitle(book1.getTitle());
//    then(bookService).should(never()).existsBookByIsbn(book1.getIsbn());
//    then(categoryService).should(never()).findCategoryById(anyInt());
//    then(bookService).should(never()).saveBook(any(Book.class));
//  }
//
//  @Test
//  @WithUserDetails
//  void addBook_whenCategoriesIsEmpty_thenReturnBodyAnd400() throws Exception {
//    Book book1 = createTestBook1();
//    given(bookService.existsBookByTitle(book1.getTitle())).willReturn(false);
//    given(bookService.existsBookByIsbn(book1.getIsbn())).willReturn(false);
//
//    mockMvc
//        .perform(
//            post("/books")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(
//                    """
//                		{
//						    "author": "%s",
//						    "categories": [
//						    ],
//						    "description": "%s",
//						    "isbn": "%s",
//						    "publicationDate": "%s",
//						    "title": "%s",
//						    "pictureUrl": "%s",
//						    "pages": "%s",
//						    "language": "%s"
//						}
//                		"""
//                        .formatted(
//                            book1.getAuthor(),
//                            book1.getDescription(),
//                            book1.getIsbn(),
//                            book1.getPublicationDate(),
//                            book1.getTitle(),
//                            book1.getPictureUrl(),
//                            book1.getPages(),
//                            book1.getLanguage()))
//                .accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isBadRequest())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//        .andExpect(jsonPath("length()", is(1)))
//        .andExpect(jsonPath("categories", is("Must not be null or empty")));
//
//    // The reason why these two are not called, is because annotations intercept
//    // the validation before it even gets to this
//    then(bookService).should(never()).existsBookByTitle(book1.getTitle());
//    then(bookService).should(never()).existsBookByIsbn(book1.getIsbn());
//    then(categoryService).should(never()).findCategoryById(anyInt());
//    then(bookService).should(never()).saveBook(any(Book.class));
//  }
//
//  // Precreate some books and categories
//
//  Book createTestBook1() {
//    Book book = new Book();
//    book.setAuthor("Patrick King");
//    book.setCategories(createTestCategories1());
//    book.setDescription(
//        "Speed read people, decipher body language, detect lies, and understand human nature. "
//            + "Is it possible to analyze people without them saying a word? Yes, it is. Learn how
// to become a “mind reader” and forge deep connections. "
//            + "How to get inside people’s heads without them knowing. "
//            + "Read People Like a Book isn’t a normal book on body language of facial expressions.
// Yes, it includes all of those things, as well as new techniques on how to truly detect lies in
// your everyday life, but this book is more about understanding human psychology and nature. We are
// who we are because of our experiences and pasts, and this guides our habits and behaviors more
// than anything else. Parts of this book read like the most interesting and applicable psychology
// textbook you’ve ever read. Take a look inside yourself and others! "
//            + "Understand the subtle signals that you are sending out and increase your emotional
// intelligence. "
//            + "Patrick King is an internationally bestselling author and social skills coach. His
// writing draws of a variety of sources, from scientific research, academic experience, coaching,
// and real life experience. "
//            + "Learn the keys to influencing and persuading others. "
//            + "•What people’s limbs can tell us about their emotions.•Why lie detecting isn’t so
// reliable when ignoring context.•Diagnosing personality as a means to understanding
// motivation.•Deducing the most with the least amount of information.•Exactly the kinds of eye
// contact to use and avoid "
//            + "Find shortcuts to connect quickly and deeply with strangers. "
//            + "The art of reading and analyzing people is truly the art of understanding human
// nature. Consider it like a cheat code that will allow you to see through people’s actions and
// words. "
//            + "Decode people’s thoughts and intentions, and you can go in any direction you want
// with them. ");
//    book.setIsbn("9798579327079");
//    book.setPublicationDate(LocalDate.of(2020, 12, 7));
//    book.setTitle(
//        "Read People Like a Book: How to Analyze, Understand, and Predict People’s Emotions,
// Thoughts, Intentions, and Behaviors");
//    book.setPictureUrl("https://m.media-amazon.com/images/I/61BqxChoN2L._SL1500_.jpg");
//    book.setPages(278);
//    book.setLanguage("English");
//
//    return book;
//  }
//
//  Book createTestBook2() {
//    Book book = new Book();
//    book.setAuthor("Emily Henry");
//    book.setCategories(createTestCategories2());
//    book.setDescription(
//        "Daphne always loved the way her fiancé Peter told their story. How they met (on a
// blustery day), fell in love (over an errant hat), and moved back to his lakeside hometown to
// begin their life together. He really was good at telling it…right up until the moment he realized
// he was actually in love with his childhood best friend Petra.\n"
//            + "Which is how Daphne begins her new story: Stranded in beautiful Waning Bay,
// Michigan, without friends or family but with a dream job as a children’s librarian (that barely
// pays the bills), and proposing to be roommates with the only person who could possibly understand
// her predicament: Petra’s ex, Miles Nowak.\n"
//            + "Scruffy and chaotic—with a penchant for taking solace in the sounds of heart break
// love ballads—Miles is exactly the opposite of practical, buttoned up Daphne, whose coworkers know
// so little about her they have a running bet that she’s either FBI or in witness protection. The
// roommates mainly avoid one another, until one day, while drowning their sorrows, they form a
// tenuous friendship and a plan. If said plan also involves posting deliberately misleading photos
// of their summer adventures together, well, who could blame them?\n"
//            + "But it’s all just for show, of course, because there’s no way Daphne would actually
// start her new chapter by falling in love with her ex-fiancé’s new fiancée’s ex…right?");
//    book.setIsbn("9780593441282");
//    book.setPublicationDate(LocalDate.of(2024, 04, 23));
//    book.setTitle("Funny Story");
//    book.setPictureUrl("https://m.media-amazon.com/images/I/71ajiVevZgL._SL1500_.jpg");
//    book.setPages(395);
//    book.setLanguage("English");
//
//    return book;
//  }
//
//  Set<Category> createTestCategories1() {
//    Category educationTeaching = new Category("Education & Teaching");
//    Category businessMoney = new Category("Business & Money");
//    Category scienceMath = new Category("Science & Math");
//    return Set.of(educationTeaching, businessMoney, scienceMath);
//  }
//
//  Set<Category> createTestCategories2() {
//    Category literatureFiction = new Category("Literature & Fiction");
//    Category romanticComedy = new Category("Romantic Comedy");
//    return Set.of(literatureFiction, romanticComedy);
//  }
// }
