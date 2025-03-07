package lt.techin.bookreservationapp.book;

import jakarta.validation.constraints.NotEmpty;

public record MessageRequestDTO(@NotEmpty String message) {}
