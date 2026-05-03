package lt.techin.bookreservationapp.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

record ResetPasswordRequestDTO(
  @NotNull String token,
  @NotNull @Size(min = 14, max = 64) String password
) {}
