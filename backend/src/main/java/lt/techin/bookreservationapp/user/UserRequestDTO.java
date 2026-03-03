package lt.techin.bookreservationapp.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record UserRequestDTO(
    // @Email validation works as such: before the @ symbol there can be at most 64
    // characters, after the @ there can be at most 63 characters
    @Email @NotNull @Size(min = 7, message = "Email must be at least 7 characters long")
        String email,
    @NotNull @Size(min = 14, max = 64) String password,
    @NotNull List<Long> roles) {}
