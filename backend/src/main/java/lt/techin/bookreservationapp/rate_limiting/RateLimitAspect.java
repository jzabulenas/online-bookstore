package lt.techin.bookreservationapp.rate_limiting;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
class RateLimitAspect {

  // Rate limiting logic lives in IpRateLimitService rather than here because LoginRateLimitFilter
  // also needs it. Servlet filters cannot invoke AOP aspects directly, so the shared sliding-window
  // map had to move to a plain Spring bean that both this aspect and the filter can inject.
  private final IpRateLimitService ipRateLimitService;

  RateLimitAspect(IpRateLimitService ipRateLimitService) {
    this.ipRateLimitService = ipRateLimitService;
  }

  @Before(
    "@annotation(lt.techin.bookreservationapp.rate_limiting.WithRateLimitProtection)"
  )
  void rateLimit() {
    final ServletRequestAttributes requestAttributes =
      (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    final String ip = requestAttributes.getRequest().getRemoteAddr();
    final String endpoint = requestAttributes.getRequest().getRequestURI();
    this.ipRateLimitService.checkLimit(ip, endpoint);
  }
}
