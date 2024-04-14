package lt.techin.bookreservationapp.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lt.techin.bookreservationapp.entities.Category;
import lt.techin.bookreservationapp.entities.User;
import lt.techin.bookreservationapp.services.CategoryService;
import lt.techin.bookreservationapp.services.UserService;

@Component
public class DatabaseInitializer implements CommandLineRunner {

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  private final CategoryService categoryService;

  public DatabaseInitializer(
      UserService userService, PasswordEncoder passwordEncoder, CategoryService categoryService) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.categoryService = categoryService;
  }

  @Override
  public void run(String... args) throws Exception {
    // Create admin
    User admin = new User();
    admin.setUsername("tony");
    admin.setPassword(passwordEncoder.encode("soprano"));
    admin.setRole("ADMIN");

    if (!userService.existsUserByUsername(admin.getUsername())) {
      userService.saveUser(admin);
    }

    // Create regular user
    User regularUser = new User();
    regularUser.setUsername("jeff");
    regularUser.setPassword(passwordEncoder.encode("goldblum"));
    regularUser.setRole("USER");

    if (!userService.existsUserByUsername(regularUser.getUsername())) {
      userService.saveUser(regularUser);
    }

    Category category1 = new Category("History");
    if (!categoryService.existsCategoryByName(category1.getName())) {
      categoryService.saveCategory(category1);
    }

    Category category2 = new Category("Self-Help");
    if (!categoryService.existsCategoryByName(category2.getName())) {
      categoryService.saveCategory(category2);
    }
  }
}
