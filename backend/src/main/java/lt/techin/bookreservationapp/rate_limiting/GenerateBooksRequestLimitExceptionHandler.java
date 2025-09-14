package lt.techin.bookreservationapp.rate_limiting;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GenerateBooksRequestLimitExceptionHandler {

  @ExceptionHandler(GenerateBooksRequestLimitException.class)
  public ResponseEntity<Object> handleTooManyBookGenerationRequests(
      GenerateBooksRequestLimitException ex) {
    Map<String, String> response = new HashMap<>();
    response.put("error", "Free users get 6 free requests a day. Please wait 24 hours.");

    return ResponseEntity.status(429).body(response);
  }
}
