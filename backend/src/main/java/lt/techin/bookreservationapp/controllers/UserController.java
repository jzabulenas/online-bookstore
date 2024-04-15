package lt.techin.bookreservationapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lt.techin.bookreservationapp.entities.User;
import lt.techin.bookreservationapp.services.UserService;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserController(UserService userService, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/signup")
  public void signup(@RequestBody User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole("USER");

    userService.saveUser(user);
  }

  @PostMapping("/login")
  public User login(@RequestBody User user) {
    User userDb = userService.findUserByUsernameAndPassword(user.getUsername(), user.getPassword());

    return userDb;
  }
}
