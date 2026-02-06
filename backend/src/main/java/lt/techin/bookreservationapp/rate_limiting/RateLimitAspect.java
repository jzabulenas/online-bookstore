package lt.techin.bookreservationapp.rate_limiting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
class RateLimitAspect {

  private static final String ERROR_MESSAGE =
      "Too many requests at endpoint %s from IP %s! Please try again after %d milliseconds!";
  private final ConcurrentHashMap<String, List<Long>> requestCounts = new ConcurrentHashMap<>();

  @Value("${APP_RATE_LIMIT:#{10}}")
  private int rateLimit;

  @Value("${APP_RATE_DURATIONINMS:#{60000}}")
  private long rateDuration;

  /**
   * Executed by each call of a method annotated with {@link WithRateLimitProtection} which should
   * be an HTTP endpoint. Counts calls per remote address. Calls older than {@link #rateDuration}
   * milliseconds will be forgotten. If there have been more than {@link #rateLimit} calls within
   * {@link #rateDuration} milliseconds from a remote address, a {@link RateLimitException} will be
   * thrown.
   *
   * @throws RateLimitException if rate limit for a given remote address has been exceeded
   */
  // TODO: Do I even need this? Because I already have the 5 requests in 24 hour
  // limit
  @Before("@annotation(lt.techin.bookreservationapp.rate_limiting.WithRateLimitProtection)")
  void rateLimit() {

    final ServletRequestAttributes requestAttributes =
        (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

    final String key = requestAttributes.getRequest().getRemoteAddr();

    // Ignore localhost requests
    if ("127.0.0.1".equals(key) || "0:0:0:0:0:0:0:1".equals(key)) {
      return;
    }

    final long currentTime = System.currentTimeMillis();

    this.requestCounts.putIfAbsent(key, new ArrayList<>());
    this.requestCounts.get(key).add(currentTime);
    this.cleanUpRequestCounts(currentTime);

    if (this.requestCounts.get(key).size() > this.rateLimit) {
      throw new RateLimitException(
          String.format(
              ERROR_MESSAGE,
              requestAttributes.getRequest().getRequestURI(),
              key,
              this.rateDuration));
    }
  }

  private void cleanUpRequestCounts(final long currentTime) {
    this.requestCounts
        .values()
        .forEach(
            l -> {
              l.removeIf(t -> this.timeIsTooOld(currentTime, t));
            });
  }

  private boolean timeIsTooOld(final long currentTime, final long timeToCheck) {
    return currentTime - timeToCheck > this.rateDuration;
  }
}
