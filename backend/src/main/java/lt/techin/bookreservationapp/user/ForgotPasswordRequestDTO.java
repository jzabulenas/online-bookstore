package lt.techin.bookreservationapp.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

record ForgotPasswordRequestDTO(
  @Email
  @NotNull
  @Size(min = 7, message = "Email must be at least 7 characters long")
  String email
) {}
