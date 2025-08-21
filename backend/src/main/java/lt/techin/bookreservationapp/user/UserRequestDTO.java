package lt.techin.bookreservationapp.user;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
    @Email @NotNull @Size(min = 7, max = 255) String email,
    @NotNull @Size(min = 8, max = 20) String password,
    @NotNull List<Long> roles) {
}
