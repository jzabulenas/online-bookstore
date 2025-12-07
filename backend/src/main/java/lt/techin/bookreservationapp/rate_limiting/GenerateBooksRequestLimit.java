package lt.techin.bookreservationapp.rate_limiting;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "generate_books_request_limits")
class GenerateBooksRequestLimit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId;
  private String endpoint;
  private Instant requestTime;

  GenerateBooksRequestLimit(Long userId, String endpoint, Instant requestTime) {
    this.userId = userId;
    this.endpoint = endpoint;
    this.requestTime = requestTime;
  }

  Long getUserId() {
    return this.userId;
  }

  void setUserId(Long userId) {
    this.userId = userId;
  }

  String getEndpoint() {
    return this.endpoint;
  }

  void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  Instant getRequestTime() {
    return this.requestTime;
  }

  void setRequestTime(Instant requestTime) {
    this.requestTime = requestTime;
  }

  Long getId() {
    return this.id;
  }

}
