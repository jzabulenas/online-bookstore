package lt.techin.bookreservationapp.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lt.techin.bookreservationapp.entities.Category;
import lt.techin.bookreservationapp.entities.User;
import lt.techin.bookreservationapp.services.UserService;

@Component
public class DatabaseInitializer implements CommandLineRunner {

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  public DatabaseInitializer(UserService userService, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
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

    Category category = new Category("History");
    //    if (userservice)
  }
}
