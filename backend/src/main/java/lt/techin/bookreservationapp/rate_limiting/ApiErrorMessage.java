package lt.techin.bookreservationapp.rate_limiting;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class ApiErrorMessage {

  private final UUID id = UUID.randomUUID();
  private final int status;
  private final String error;
  private final String message;
  private final LocalDateTime timestamp = LocalDateTime.now(Clock.systemUTC());
  private final String path;

  ApiErrorMessage(int status, String error, String message, String path) {
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
  }

  UUID getId() {
    return id;
  }

  int getStatus() {
    return status;
  }

  String getError() {
    return error;
  }

  String getMessage() {
    return message;
  }

  LocalDateTime getTimestamp() {
    return timestamp;
  }

  String getPath() {
    return path;
  }
}
