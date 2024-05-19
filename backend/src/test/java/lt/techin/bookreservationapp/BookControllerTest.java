package lt.techin.bookreservationapp;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import lt.techin.bookreservationapp.controllers.BookController;
import lt.techin.bookreservationapp.entities.Book;
import lt.techin.bookreservationapp.entities.Category;
import lt.techin.bookreservationapp.repositories.BookRepository;
import lt.techin.bookreservationapp.security.SecurityConfig;
import lt.techin.bookreservationapp.services.BookService;
import lt.techin.bookreservationapp.services.CategoryService;

@WebMvcTest(controllers = BookController.class)
@Import(SecurityConfig.class)
public class BookControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private BookService bookService;

  @MockBean private BookRepository bookRepository;

  @MockBean private CategoryService categoryService;

  @Test
  @WithUserDetails
  void getBooks_whenAuthenticatedCalls_thenReturnBooksAnd200() throws Exception {
    Book book = createTestBook1();

    given(bookService.findAllBooks()).willReturn(List.of(book));

    mockMvc.perform(get("/books")).andExpect(status().isOk());
  }

  Book createTestBook1() {
    Book book = new Book();
    book.setAuthor("Patrick King");
    book.setCategories(createTestCategories1());
    book.setDescription(
        " Speed read people, decipher body language, detect lies, and understand human nature.\n"
            + "Is it possible to analyze people without them saying a word? Yes, it is. Learn how to become a “mind reader” and forge deep connections.\n"
            + "How to get inside people’s heads without them knowing.\n"
            + "Read People Like a Book isn’t a normal book on body language of facial expressions. Yes, it includes all of those things, as well as new techniques on how to truly detect lies in your everyday life, but this book is more about understanding human psychology and nature. We are who we are because of our experiences and pasts, and this guides our habits and behaviors more than anything else. Parts of this book read like the most interesting and applicable psychology textbook you’ve ever read. Take a look inside yourself and others!\n"
            + "Understand the subtle signals that you are sending out and increase your emotional intelligence.\n"
            + "Patrick King is an internationally bestselling author and social skills coach. His writing draws of a variety of sources, from scientific research, academic experience, coaching, and real life experience.\n"
            + "Learn the keys to influencing and persuading others.\n"
            + "•What people’s limbs can tell us about their emotions.•Why lie detecting isn’t so reliable when ignoring context.•Diagnosing personality as a means to understanding motivation.•Deducing the most with the least amount of information.•Exactly the kinds of eye contact to use and avoid\n"
            + "Find shortcuts to connect quickly and deeply with strangers.\n"
            + "The art of reading and analyzing people is truly the art of understanding human nature. Consider it like a cheat code that will allow you to see through people’s actions and words.\n"
            + "Decode people’s thoughts and intentions, and you can go in any direction you want with them.");
    book.setIsbn("9798579327079");
    book.setPublicationDate(LocalDate.of(2020, 12, 7));
    book.setTitle(
        "Read People Like a Book: How to Analyze, Understand, and Predict People’s Emotions, Thoughts, Intentions, and Behaviors");
    book.setPictureUrl("https://m.media-amazon.com/images/I/61BqxChoN2L._SL1500_.jpg");
    book.setPages(278);
    book.setLanguage("English");

    return book;
  }

  Book createTestBook2() {
    Book book = new Book();
    book.setAuthor("Emily Henry");
    book.setCategories(createTestCategories2());
    book.setDescription(
        "Daphne always loved the way her fiancé Peter told their story. How they met (on a blustery day), fell in love (over an errant hat), and moved back to his lakeside hometown to begin their life together. He really was good at telling it…right up until the moment he realized he was actually in love with his childhood best friend Petra.\n"
            + "Which is how Daphne begins her new story: Stranded in beautiful Waning Bay, Michigan, without friends or family but with a dream job as a children’s librarian (that barely pays the bills), and proposing to be roommates with the only person who could possibly understand her predicament: Petra’s ex, Miles Nowak.\n"
            + "Scruffy and chaotic—with a penchant for taking solace in the sounds of heart break love ballads—Miles is exactly the opposite of practical, buttoned up Daphne, whose coworkers know so little about her they have a running bet that she’s either FBI or in witness protection. The roommates mainly avoid one another, until one day, while drowning their sorrows, they form a tenuous friendship and a plan. If said plan also involves posting deliberately misleading photos of their summer adventures together, well, who could blame them?\n"
            + "But it’s all just for show, of course, because there’s no way Daphne would actually start her new chapter by falling in love with her ex-fiancé’s new fiancée’s ex…right?");
    book.setIsbn("9780593441282");
    book.setPublicationDate(LocalDate.of(2024, 04, 23));
    book.setTitle("Funny Story");
    book.setPictureUrl("https://m.media-amazon.com/images/I/71ajiVevZgL._SL1500_.jpg");
    book.setPages(395);
    book.setLanguage("English");

    return book;
  }

  List<Category> createTestCategories1() {
    Category educationTeaching = new Category("Education & Teaching");
    Category businessMoney = new Category("Business & Money");
    Category scienceMath = new Category("Science & Math");
    return List.of(educationTeaching, businessMoney, scienceMath);
  }

  List<Category> createTestCategories2() {
    Category literatureFiction = new Category("Literature & Fiction");
    Category romanticComedy = new Category("Romantic Comedy");
    return List.of(literatureFiction, romanticComedy);
  }
}
