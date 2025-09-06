package lt.techin.bookreservationapp.rate_limiting;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GenerateBooksRateLimitService {

  private final GenerateBooksRequestLimitRepository requestLimitRepository;

  @Value("${app.rate-limit.max-requests:5}")
  private int maxRequests;

  @Autowired
  public GenerateBooksRateLimitService(GenerateBooksRequestLimitRepository requestLimitRepository) {
    this.requestLimitRepository = requestLimitRepository;
  }

  public void checkLimit(Long userId, String endpoint) {
    // Subtract 24 hours from today. Result is yesterday, same time as today
    Instant since = Instant.now().minus(24, ChronoUnit.HOURS);

    // Counts how many requests a particular user, with particular id, to particular
    // endpoint, since a particular date made
    // In other words, if there were requests made in the time frame between
    // yesterday and today, that is, in those previous 24 hours, they are counted
    long count = requestLimitRepository
        .countByUserIdAndEndpointAndRequestTimeAfter(userId, endpoint, since);

    if (count >= maxRequests) {
      throw new GenerateBooksRequestLimitException();
    }

    // Save request log
    GenerateBooksRequestLimit rl = new GenerateBooksRequestLimit(userId, endpoint,
        Instant.now());
    this.requestLimitRepository.save(rl);
  }
}
