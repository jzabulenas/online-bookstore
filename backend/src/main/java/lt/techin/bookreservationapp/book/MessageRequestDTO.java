package lt.techin.bookreservationapp.book;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MessageRequestDTO(@NotNull @Size(min = 5, max = 100) String message) {}
