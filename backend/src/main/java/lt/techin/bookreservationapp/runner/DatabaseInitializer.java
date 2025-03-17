package lt.techin.bookreservationapp.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lt.techin.bookreservationapp.user.UserService;

@Component
// It will always match default application.properties, as it does not match any else
@Profile("default")
class DatabaseInitializer implements CommandLineRunner {

  private final UserService userService;

  // private final CategoryService categoryService;

  // private final BookService bookService;

  DatabaseInitializer(UserService userService) {
    this.userService = userService;
    // this.categoryService = categoryService;
    // this.bookService = bookService;
  }

  @Override
  public void run(String... args) throws Exception {
    // Create admin
    //    User admin = new User();
    //    admin.setUsername("tony");
    //    admin.setPassword(passwordEncoder.encode("soprano"));
    //    admin.setRole("ADMIN");
    //
    //    if (!userService.existsUserByUsername(admin.getUsername())) {
    //      userService.saveUser(admin);
    //    }

    // Create regular user
    //    User regularUser = new User();
    //    regularUser.setUsername("jeff");
    //    regularUser.setPassword(passwordEncoder.encode("goldblum"));
    //    regularUser.setRole("USER");
    //
    //    if (!userService.existsUserByUsername(regularUser.getUsername())) {
    //      userService.saveUser(regularUser);
    //    }

    // Categories
    //    Category category1 = new Category("History");
    //    if (!categoryService.existsCategoryByName(category1.getName())) {
    //      categoryService.saveCategory(category1);
    //    }

    //    Category category2 = new Category("Self-Help");
    //    if (!categoryService.existsCategoryByName(category2.getName())) {
    //      categoryService.saveCategory(category2);
    //    }

    // Books
    //    Category categoryFromDb1 = categoryService.findCategoryByName(category1.getName());

    //    Book book1 =
    //        new Book(
    //            "The Canterbury Tales: Seventeen Tales and the General Prologue: A Norton Critical
    // Edition (Third Edition) (Norton Critical Editions)",
    //            "Geoffrey Chaucer",
    //            Set.of(categoryFromDb1),
    //            "This book has been more helpful to the students—both the better ones and the
    // lesser ones—than any other book I have ever used in any of my classes in my more than a
    // quarter century of university teaching.",
    //            "https://m.media-amazon.com/images/I/91sxb8tSpWL._SL1500_.jpg",
    //            704,
    //            "9781234567897",
    //            LocalDate.of(2018, 6, 1),
    //            "English");

    //    if (!bookService.existsBookByIsbn(book1.getIsbn())) {
    //      bookService.saveBook(book1);
    //    }

    //    Category categoryFromDb2 = categoryService.findCategoryByName(category2.getName());

    //    Book book2 =
    //        new Book(
    //            "Atomic Habits: An Easy & Proven Way to Build Good Habits & Break Bad Ones",
    //            "James Clear",
    //            Set.of(categoryFromDb2),
    //            "No matter your goals, Atomic Habits offers a proven framework for
    // improving--every day. James Clear, one of the world's leading experts on habit formation,
    // reveals practical strategies that will teach you exactly how to form good habits, break bad
    // ones, and master the tiny behaviors that lead to remarkable results.",
    //            "https://m.media-amazon.com/images/I/81Z+AlCF3tL._SL1500_.jpg",
    //            319,
    //            "9781234567898",
    //            LocalDate.of(2018, 10, 16),
    //            "English");

    //    if (!bookService.existsBookByIsbn(book2.getIsbn())) {
    //      bookService.saveBook(book2);
    //    }
  }
}
