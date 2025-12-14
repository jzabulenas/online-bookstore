package lt.techin.bookreservationapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lt.techin.bookreservationapp.user.UserService;

@Service
class UserDetailsServiceImpl implements UserDetailsService {

  private final UserService userService;

  @Autowired
  UserDetailsServiceImpl(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.userService.findUserByEmail(username);
  }
}
