package lt.techin.bookreservationapp.book;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class BookExceptionHandler {

  @ExceptionHandler(BookTitleAlreadyExistsException.class)
  ResponseEntity<Map<String, String>> handleBookAlreadyExistsException() {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("title", "Already exists");

    return ResponseEntity.badRequest().body(errorResponse);
  }
}
