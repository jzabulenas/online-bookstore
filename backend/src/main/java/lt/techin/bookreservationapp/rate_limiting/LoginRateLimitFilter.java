package lt.techin.bookreservationapp.rate_limiting;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

@Component
public class LoginRateLimitFilter extends OncePerRequestFilter {

  private final IpRateLimitService ipRateLimitService;
  private final ObjectMapper objectMapper;

  LoginRateLimitFilter(
    IpRateLimitService ipRateLimitService,
    ObjectMapper objectMapper
  ) {
    this.ipRateLimitService = ipRateLimitService;
    this.objectMapper = objectMapper;
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    if (
      HttpMethod.POST.matches(request.getMethod()) &&
      "/login".equals(request.getServletPath())
    ) {
      try {
        this.ipRateLimitService.checkLimit(request.getRemoteAddr(), "/login");
      } catch (RateLimitException e) {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response
          .getWriter()
          .write(
            this.objectMapper.writeValueAsString(
              e.toApiErrorMessage(request.getServletPath())
            )
          );
        return;
      }
    }

    filterChain.doFilter(request, response);
  }
}
