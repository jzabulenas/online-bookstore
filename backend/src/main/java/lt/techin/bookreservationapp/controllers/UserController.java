package lt.techin.bookreservationapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import lt.techin.bookreservationapp.services.UserService;

// @CrossOrigin("http://localhost:5173")
@RestController
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  //  @PostMapping("/signup")
  //  public void signup(@RequestBody User user) {
  //    user.setPassword(passwordEncoder.encode(user.getPassword()));
  //    user.setRole("USER");
  //
  //    userService.saveUser(user);
  //  }

  //  @PostMapping("/login")
  //  public User login(@RequestBody User user) {
  //    User userDb = userService.findUserByUsernameAndPassword(user.getUsername(),
  // user.getPassword());
  //
  //    return userDb;
  //  }
}
