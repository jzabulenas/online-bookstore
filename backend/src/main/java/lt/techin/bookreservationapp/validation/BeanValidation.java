package lt.techin.bookreservationapp.validation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lt.techin.bookreservationapp.user.EmailAlreadyExistsException;

@RestControllerAdvice
class BeanValidation {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });

    return errors;
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  ResponseEntity<Object> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
    Map<String, String> response = new HashMap<>();
    response.put("email", "Such email address is already in use");

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(CompromisedPasswordException.class)
  ProblemDetail handle(CompromisedPasswordException exception) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
  }
}
