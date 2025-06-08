package lt.techin.bookreservationapp.rate_limiting;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
class RateLimitException extends RuntimeException {

  RateLimitException(final String message) {
    super(message);
  }

  ApiErrorMessage toApiErrorMessage(final String path) {
    return new ApiErrorMessage(
        HttpStatus.TOO_MANY_REQUESTS.value(),
        HttpStatus.TOO_MANY_REQUESTS.name(),
        this.getMessage(),
        path);
  }
}
