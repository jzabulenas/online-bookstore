package lt.techin.bookreservationapp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.validation.Valid;
import lt.techin.bookreservationapp.role.Role;

// TOOD: add /api? Also, maybe add /users?
@RestController
class UserController {

  private final UserService userService;
  private final UserRepository userRepository;
  private final String frontendUrl;

  @Autowired
  UserController(
      UserService userService,
      UserRepository userRepository,
      @Value("${FRONTEND_URL}") String frontendUrl) {
    this.userService = userService;
    this.userRepository = userRepository;
    this.frontendUrl = frontendUrl;
  }

  // TODO: change this to /users? Because the Location header is something like
  // this: /signup/113
  @PostMapping("/signup")
  ResponseEntity<Object> signup(@RequestBody @Valid UserRequestDTO userRequestDTO) {
    UserResponseDTO savedUser = this.userService.saveUser(userRequestDTO);

    return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.id())
                .toUri())
        .body(savedUser);
  }

  @GetMapping("/user")
  UserAuthenticationResponseDTO getUser(Authentication authentication) {
    User user = (User) authentication.getPrincipal();

    return new UserAuthenticationResponseDTO(
        user.getEmail(), user.getRoles().stream().map(Role::getName).toList());
  }

  @GetMapping("/open")
  String openCall() {
    return "This is an open endpoint";
  }

  @GetMapping("/verify")
  RedirectView verify(@RequestParam String code) {
    User user = this.userService.findUserByVerificationCode(code);
    user.setEnabled(true);
    user.setVerificationCode(null);

    this.userService.saveUser(user);

    return new RedirectView(this.frontendUrl + "/verification-success");
  }
}
