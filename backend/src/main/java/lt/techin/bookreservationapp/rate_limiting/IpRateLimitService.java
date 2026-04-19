package lt.techin.bookreservationapp.rate_limiting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IpRateLimitService {

  private final ConcurrentHashMap<String, List<Long>> requestCounts =
    new ConcurrentHashMap<>();

  @Value("${APP_RATE_LIMIT:#{10}}")
  private int rateLimit;

  @Value("${APP_RATE_DURATIONINMS:#{60000}}")
  private long rateDuration;

  public void checkLimit(String ip, String endpoint) {
    if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
      return;
    }

    String key = ip + ":" + endpoint;
    long currentTime = System.currentTimeMillis();

    this.requestCounts.putIfAbsent(key, new ArrayList<>());
    this.requestCounts.get(key).add(currentTime);
    this.requestCounts.values().forEach(l ->
      l.removeIf(t -> currentTime - t > this.rateDuration)
    );

    if (this.requestCounts.get(key).size() > this.rateLimit) {
      throw new RateLimitException(
        String.format(
          "Too many requests at endpoint %s from IP %s! Please try again after %d milliseconds!",
          endpoint,
          ip,
          this.rateDuration
        )
      );
    }
  }
}
