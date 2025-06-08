package lt.techin.bookreservationapp.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// @CrossOrigin("http://localhost:5173")
@RestController
class UserController {

  private final UserService userService;

  @Autowired
  UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/signup")
  ResponseEntity<Object> signup(@RequestBody UserRequestDTO userRequestDTO) {
    if (this.userService.existsUserByEmail(userRequestDTO.email())) {
      Map<String, String> response = new HashMap<>();
      response.put("username", "Already exists");

      return ResponseEntity.badRequest().body(response);
    }

    return ResponseEntity.ok(this.userService.saveUser(userRequestDTO));
  }

  // @PostMapping("/login")
  // public User login(@RequestBody User user) {
  // User userDb = userService.findUserByUsernameAndPassword(user.getUsername(),
  // user.getPassword());
  //
  // return userDb;
  // }

  @GetMapping("/user")
  Authentication getUser(Authentication authentication) {
    return authentication;
  }

  @GetMapping("/open")
  String openCall() {
    return "This is an open endpoint";
  }
}
