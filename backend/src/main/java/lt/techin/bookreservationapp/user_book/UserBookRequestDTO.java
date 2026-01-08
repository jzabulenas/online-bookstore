package lt.techin.bookreservationapp.user_book;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserBookRequestDTO(@NotNull @Size(min = 5, max = 255) String title) {}
