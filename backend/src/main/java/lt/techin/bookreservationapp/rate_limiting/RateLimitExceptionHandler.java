package lt.techin.bookreservationapp.rate_limiting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class RateLimitExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(RateLimitExceptionHandler.class);

  @ExceptionHandler(RateLimitException.class)
  ResponseEntity<ApiErrorMessage> handleInvalidFieldsInValidJson(
      final RateLimitException rateLimitException, final HttpServletRequest request) {

    final ApiErrorMessage apiErrorMessage = rateLimitException
        .toApiErrorMessage(request.getRequestURI());
    logIncomingCallException(rateLimitException, apiErrorMessage);

    return new ResponseEntity<>(apiErrorMessage, HttpStatus.TOO_MANY_REQUESTS);
  }

  private static void logIncomingCallException(
      final RateLimitException rateLimitException, final ApiErrorMessage apiErrorMessage) {

    LOG.error(String.format("%s: %s", apiErrorMessage.getId(), rateLimitException
        .getMessage()), rateLimitException);
  }
}
