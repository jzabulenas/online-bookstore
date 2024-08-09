package lt.techin.bookreservationapp.security;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lt.techin.bookreservationapp.entities.User;
import lt.techin.bookreservationapp.repositories.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  public CustomOAuth2UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);

    // Extract attributes from OAuth2User
    Map<String, Object> attributes = oAuth2User.getAttributes();
    String email = (String) attributes.get("email");

    // Fetch or create the user in your database
    User user = userRepository.findByEmail(email).orElseGet(() -> createUserFromOAuth2(attributes));

    user.setAttributes(attributes); // Save OAuth2 attributes

    // Return your custom User object
    return user;
  }

  private User createUserFromOAuth2(Map<String, Object> attributes) {
    User user = new User();
    user.setEmail((String) attributes.get("email"));
    //    user.setName((String) attributes.get("name"));
    user.setRole("ROLE_USER");
    userRepository.save(user);
    return user;
  }
}
