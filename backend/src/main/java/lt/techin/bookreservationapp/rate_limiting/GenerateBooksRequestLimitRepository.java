package lt.techin.bookreservationapp.rate_limiting;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenerateBooksRequestLimitRepository
    extends JpaRepository<GenerateBooksRequestLimit, Long> {

  long countByUserIdAndEndpointAndRequestTimeAfter(
      Long userId, String endpoint, Instant after);
}
