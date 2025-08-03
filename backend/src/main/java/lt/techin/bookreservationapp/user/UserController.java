package lt.techin.bookreservationapp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lt.techin.bookreservationapp.role.Role;

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
  UserAuthenticationResponseDTO getUser(Authentication authentication) {
    User user = (User) authentication.getPrincipal();

    return new UserAuthenticationResponseDTO(
        user.getEmail(),
        user.getRoles()
            .stream()
            .map(Role::getName)
            .toList());
  }

  @GetMapping("/open")
  String openCall() {
    return "This is an open endpoint";
  }
}
