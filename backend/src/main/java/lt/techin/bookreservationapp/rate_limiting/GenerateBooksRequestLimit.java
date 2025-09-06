package lt.techin.bookreservationapp.rate_limiting;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "generate_books_request_limits")
public class GenerateBooksRequestLimit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId;
  private String endpoint;
  private Instant requestTime;

  public GenerateBooksRequestLimit(Long userId, String endpoint, Instant requestTime) {
    this.userId = userId;
    this.endpoint = endpoint;
    this.requestTime = requestTime;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public Instant getRequestTime() {
    return requestTime;
  }

  public void setRequestTime(Instant requestTime) {
    this.requestTime = requestTime;
  }

  public Long getId() {
    return id;
  }

}
