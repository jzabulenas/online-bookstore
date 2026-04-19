package lt.techin.bookreservationapp.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

class HttpStatusHandler
  implements
    AuthenticationFailureHandler,
    AuthenticationSuccessHandler,
    LogoutSuccessHandler,
    AuthenticationEntryPoint
{

  private final HttpStatus status;

  HttpStatusHandler(HttpStatus status) {
    this.status = status;
  }

  private void handle(HttpServletResponse response) {
    response.setStatus(this.status.value());
  }

  @Override
  public void onAuthenticationFailure(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException exception
  ) {
    this.handle(response);
  }

  @Override
  public void onAuthenticationSuccess(
    HttpServletRequest request,
    HttpServletResponse response,
    Authentication authentication
  ) {
    this.handle(response);
  }

  @Override
  public void onLogoutSuccess(
    HttpServletRequest request,
    HttpServletResponse response,
    @Nullable Authentication authentication
  ) {
    this.handle(response);
  }

  @Override
  public void commence(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException authException
  ) {
    this.handle(response);
  }
}
